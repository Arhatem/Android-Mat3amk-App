import requests
from bs4 import BeautifulSoup
import json
import os
import argparse

def scrap(URL):

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

if __name__ == "__main__":

    parser = argparse.ArgumentParser()
    parser.add_argument("URL", help="url from tripadvisor to scrab data from", type=str)
    args = parser.parse_args()

    json_file = scrap(args.URL)

    dirName = os.path.join(os.getcwd(), "Database", json_file["resturant_name"])
    
    if not os.path.exists(dirName):
        os.makedirs(dirName)
    
    r = requests.get(json_file["image_url"], stream=True)

    with open(os.path.join(dirName, 'pic.jpg'), 'wb') as f:
        f.write(r.content) 

    with open(os.path.join(dirName,'data.json'), 'w') as outfile:
        json.dump(json_file, outfile)

# For testing purposes 

# url_1 = "https://www.tripadvisor.com/Restaurant_Review-g294201-d2700915-Reviews-The_Grill_Restaurant_Lounge-Cairo_Cairo_Governorate.html"
# url_2 = "https://www.tripadvisor.com/Restaurant_Review-g294201-d2619464-Reviews-Fayruz_Lebanese_Restaurant-Cairo_Cairo_Governorate.html"
# url_3 = "https://www.tripadvisor.com/Restaurant_Review-g294201-d1025515-Reviews-Birdcage-Cairo_Cairo_Governorate.html"

# print(scrap(url_1))
