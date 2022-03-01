
# Önkiszólgáló kassza project
A projekt célja egy önkiszolgáló kassza webalkalmazás fejlesztése volt.
A fejlesztés Spring keretrendszerben történt, az egyes felületek pedig a ThymeLeaf segítségével lettek megjelenítve.
A designe megtervezésére és megalkotására CSS Bootstrap-et használtunk.  
  
<strong>Vásárlóval a belépés: Felh: customer, jelsz: customer</strong>
## Adatbázis
Az egyes adatokat MySql adatbázisban tároltuk.
* https://www.mysql.com/
#### Az alábbi ábra mutatja az adatbázis szerkezetét
![](src/main/resources/database.png)
## Vonalkód olvasás
A vonalkód beolvasására az alábbi eszközt használtuk:  
https://www.zebra.com/content/dam/zebra_new_ia/en-us/manuals/barcode-scanners/ls2208-product-reference-guide-en-us.pdf  
A beolvasót úgy konfiguráltuk, hogy a következő módon küldeje a beolvasott információt:  
* \<DATA\>\<SUFFIX\>,
ahol suffix-nek "Enter" ütést választottunk. A felületbe történő integrálást JavaScript segítségével végeztük.
## Tesztek
* Unit tesztek
* Spring boot tesztek
* Spring MVC tesztek
