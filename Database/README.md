## Data Scraping 

We developed here a python script for automating database generation with scraping data from famous web sites like **Tripadvisor** & **Elmenus**.

## Dependencies

``` pip3 install --user -r requirments.txt ```

## Usage

The script uses the default ```Resturants_urls``` file to download the data from this resturants.

**Warning**: The script only works with the specified urls in the file ... something like this

```https://www.elmenus.com/cairo/delivery/<Area goes here>```

You can add as many links as you want it will only download what it can't find in the ```database.json``` file.

The script has a ```-i``` parameter where you can specifiy your own text file of urls **but** make sure you remove the ```database.json``` file if you want a fully new database for your work.

if you made everything right you can just type this in your command line

```python3 Data_scraper.py```

***And let the Magic Happens ^ ^***
