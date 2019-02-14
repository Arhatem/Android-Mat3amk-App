import requests
from bs4 import BeautifulSoup
from selenium import webdriver
import json
import os
import argparse
import re

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
    dish = []

    resturant_name = soup.findAll("h1", {"class": "title"})[0].text.strip()

    image_url = soup.findAll("img", {"class": "v-center"})[0]['src']

    rating = float(len(soup.findAll("li", {"class": "star active"})))

    tmp = soup.findAll("p", {"class": "info-value"})[2].text.replace('\n','')
    address = tmp[:tmp.find("+")].strip()

    phone = soup.findAll("p", {"class": "info-value"})[1].text.replace('\n','').replace(' ','')

    for meal in soup.findAll("div", {"class": "cat-section"}):
        category_name = meal.find("h3", {"class": "section-title"}).text.replace('\n','')[:-3].strip()
        
        dish.clear()
        for food in meal.findAll("div", {"class": "content"}):
            name = food.find("h5").text.replace('\n','').strip()
            try:
                price = food.find("span", {"class": "bold"}).text.replace('\n','').strip()
            except:
                continue
            dish.append(name + ", " + price + " EGP")
            
        categories[category_name] = dish[:]

    data = {"name": resturant_name,
            "rating": rating,
            "address": address,
            "image_url": image_url,
            "phone": phone,
            "categories": categories}

    browser.quit()

    return data

if __name__ == "__main__":

    parser = argparse.ArgumentParser()
    parser.add_argument("URL", help="url from tripadvisor to scrab data from", type=str)
    args = parser.parse_args()
    
    browser = webdriver.Chrome('./chromedriver') 
    browser.get(args.URL)
    soup = BeautifulSoup(browser.page_source, "lxml")
    
    Location = args.URL[args.URL.rfind("/")+1:].replace("-", " ").title()

    for rest in soup.find_all("div", {"class": "slick-track"})[-1].find_all("a"):

        url = args.URL[:args.URL.find(rest['href'][rest['href'].find("/"):rest['href'].rfind("/")])] + rest['href']

        try:
                
            json_file = scrap_elmenus(url)

            dirName = os.path.join(os.getcwd(), "Database", Location ,json_file["name"])
            
            if not os.path.exists(dirName):
                os.makedirs(dirName)
            
            r = requests.get(json_file["image_url"], stream=True)

            with open(os.path.join(dirName, 'pic.jpg'), 'wb') as f:
                f.write(r.content) 
            

            with open(os.path.join(dirName,'data.json'), 'w') as outfile:
                json.dump(json_file, outfile)
            
            print("Resturant {} has Finished Successfully".format(json_file["name"]))

        except:
             
            print("Resturant {} has Failed Successfully".format(json_file["name"]))
            continue
            

    browser.quit()
    print(" ----- Done! ----- ")