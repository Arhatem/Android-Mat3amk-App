import requests
from bs4 import BeautifulSoup
from selenium import webdriver
import json
import os
import argparse

def scrap_tripadvisor(URL):

    # Page request with html parsing for scrabbing
    page = requests.get(URL)
    soup = BeautifulSoup(page.content, "lxml")
    data = {}

    resturant_name = soup.findAll("h1", {"class": "ui_header h1"})[0].text

    image_url = soup.findAll("img", {"class": "basicImg"})[0]['src']

    rating = float(soup.findAll("span", {"class": "restaurants-detail-overview-cards-RatingsOverviewCard__overallRating--r2Cf6"})[0].text)

    street_address = soup.findAll("span", {"class": "street-address"})[0].text
    extended_address = soup.findAll("span", {"class": "extended-address"})[0].text
    locality = soup.findAll("span", {"class": "locality"})[0].text
    country_name = soup.findAll("span", {"class": "country-name"})[0].text
    
    address = street_address + " | " + extended_address + ", " + locality + country_name

    phone = soup.findAll("span", {"class": "is-hidden-mobile detail"})[0].text

    data = {"resturant_name": resturant_name,
            "image_url": image_url,
            "rating": rating,
            "address": address,
            "phone": phone}

    return data

def scrap_elmenus(URL):

    # Page request with html parsing for scrabbing
    browser = webdriver.Chrome('./chromedriver') 
    browser.get(URL)
    soup = BeautifulSoup(browser.page_source, "lxml")
    data = {}
    categories = {}
    dish = {}

    resturant_name = soup.findAll("h1", {"class": "title"})[0].text.strip()

    image_url = soup.findAll("img", {"class": "v-center"})[0]['src']

    rating = float(len(soup.findAll("li", {"class": "star active"})))

    tmp = soup.findAll("p", {"class": "info-value"})[2].text.replace('\n','')
    address = tmp[:tmp.find("+")].strip()

    phone = soup.findAll("p", {"class": "info-value"})[1].text.replace('\n','').replace(' ','')

    for meal in soup.findAll("div", {"class": "cat-section"}):
        category_name = meal.find("h3", {"class": "section-title"}).text.replace('\n','')[:-3].strip()

        dish.clear()
        for food in meal.findAll("div", {"class": "menu-item clickable-item "}):
            name = food.find("h5").text.replace('\n','').strip()
            try:
                description = food.find("p", {"class": "description"}).text.replace('\n','').strip()
                if description is None:
                    description = "None"    
            except:
                description = "None"
            
            try:
                price = food.find("span", {"class": "bold"}).text.replace('\n','').strip() + " EGP"
                if price is None:
                    price = "Undefined"
            except:
                price = "Undefined"
            
            try:
                img = food.find("img")["src"]
            except:
                img = "https://mamadips.com/wp-content/uploads/2016/11/defimage.gif"
            
            dish[name] = {"dish_name": name,
                         "description": description,
                         "price": price,
                         "image_URL": img
                         }
        
            
        categories[category_name] = dish.copy()

    data = {"name": resturant_name,
            "rating": rating,
            "address": address,
            "image_url": image_url,
            "phone": phone,
            "categories": categories}

    browser.quit()

    return data

# url_1 = "https://www.elmenus.com/cairo/city-crepe-8aww"
# print(scrap_elmenus(url_1))
                      
if __name__ == "__main__":

    Database = {}
    db = {}

    parser = argparse.ArgumentParser()
    parser.add_argument("-p", "--Path", help="path to urls to download", type=str, default="Resturants_urls")
    args = parser.parse_args()
    
    with open(args.Path, 'r') as f:
        URLs = list(filter(None, f.read().split("\n")))
    
    for URL in URLs:
        
        Location = URL[URL.rfind("/")+1:].replace("-", " ").title()

        if os.path.isfile('./database.json'):
            with open("./database.json", 'r') as f:
                Database = json.load(f)

            if Location in Database:
                print("Skipped :-)")
                continue

        browser = webdriver.Chrome('./chromedriver') 
        browser.get(URL)
        soup = BeautifulSoup(browser.page_source, "lxml")
        
        db.clear()
        for rest in soup.find_all("a", {"tabindex": "0", "href": True}):

            url = URL[:URL.find(rest['href'][rest['href'].find("/"):rest['href'].rfind("/")])] + rest['href']

            try:
                json_file = scrap_elmenus(url)
                print("Success^ ^")
            except:
                print("Failed!!!")
                continue

            db[json_file["name"]] = json_file.copy()
        
        Database[Location] = db.copy()

        print("Database Updated")

        browser.quit()
    
        with open(os.path.join(os.getcwd(),'database.json'), 'w') as outfile:
            json.dump(Database, outfile, indent=4, sort_keys=True)
    
    print(" ----- Done! ----- ")