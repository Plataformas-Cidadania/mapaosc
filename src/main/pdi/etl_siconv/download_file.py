# -*- coding: utf-8 -*-

'''
Created on 07/09/2015

@author: vagnerpraia
'''

from csv import reader, writer, QUOTE_ALL
from urllib2 import urlopen
from sys import argv

url = argv[1]
path = argv[2]
filename = argv[3]

response = urlopen(url) # Download do arquivo
csv_reader = reader(response, delimiter = ';')
csv_file = path + '/' + filename

with open(csv_file, 'wb') as csv_output:
    wr = writer(csv_output, delimiter=',', quoting = QUOTE_ALL)
    for c in csv_reader:
        wr.writerow(c)