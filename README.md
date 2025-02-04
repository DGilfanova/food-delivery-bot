# **🤖 Food-delivery-bot**
 A chatbot for a food delivery service using Stream and Batch processing   

### **:star:Project objective**

Telegram bot is designed for food delivery from various cafes and restaurants. Must ensure convenient interaction between the client, the courier and catering establishments.

## **:white_check_mark: Bot functionality**
### User functionality
|1|:memo:Registration and authorization|Possibility of registration via phone number|   
|:-|:-|:-|
|1.1| |Authorization via phone number|
|**2**|**:mag_right:Search and selection of establishments**|**Search by name, cuisine and type of dish**| 
|2.1| |Filter by rating, delivery time and price |
|2.2| |Ability to view reviews and ratings of the establishment. |
|**3**|**🧺Selecting dishes and placing an order** |**View menu with photos of dishes, descriptions and prices** |
|3.1| |Adding dishes to the cart with quantity |
|3.2| |Ability to specify special wishes for the order (for example, "without onions") |
|3.3| |Choice of payment method (online or cash on delivery) |
|3.4| |Specify the delivery address with the ability to save multiple addresses. |
|**4**|**:round_pushpin:Order tracking** |**Order status notifications (accepted, being prepared, transferred to courier, delivered)** |
|4.1| |Possibility to track the location of the courier on the map|
|4.2| |Contact courier in case of problems with your order |
|**5**|**:blue_book:Order history** |**View history of completed orders** |
|5.1| |Possibility to repeat the previous order |
|5.2| |Rate and leave feedback about the order |
|**6**|**:speech_balloon:Support** |**Possibility to contact support via chat** |
|6.1||Frequently asked questions (FAQ) for quick problem solving|
|**7**|**:leftwards_arrow_with_hook:Cancellation and refund**|**Ability to cancel an order before it is prepared**|
|7.1||Refund procedure in case of cancellation or problems with the order|
|**8**|**:gift:Promotions and discounts**|**Notifications about current promotions and special offers**|
|8.1||Applying promotional codes and discounts to your order|

### Business functionality

|1|:memo:Registration and authorization|Registration and account management|   
|:-|:-|:-|
|1.1||Possibility of registering an establishment with the name, type of cuisine, address and contact information|
|1.2||Account verification via moderation (data verification)|
|1.3||Editing the establishment profile (description, logo, photos, contact information)|
|**2**|**:wrench:Menu Management**|**Add, edit and delete dishes and categories**|
|2.1||Indication of name, description, price, photo and weight for each dish|
|2.2||Possibility to mark special offers, vegetarian, vegan or gluten-free dishes|
|2.3||Managing the availability of dishes (on/off when ingredients are missing)|
|**3**|**⚙️Receiving and processing orders**|**Receive notifications about new orders in real time**|
|3.1||View order details (composition, delivery address, time, special customer requests)|
|3.2||Possibility to confirm or cancel the order|
|**4**|**:car:Delivery management**|**Indication of cooking time for each dish**|
|4.1||Tracking delivery status (delivered to courier, in transit, delivered)|
|**5**|**:moneybag:Pricing and discounts**|**Setting prices for dishes**|
|5.1||Creation of promotions, discounts and promo codes|
|5.2||Possibility to set a minimum order amount|
|**6**|**:bar_chart:Analytics and reports**|**View statistics on orders (quantity, amount, popular dishes)**|
|6.1||Generating reports on revenue, number of orders and other metrics|
|**7**|**:notebook:Managing Reviews**|**View customer reviews and ratings**|
|7.1||Ability to reply to reviews|
|7.2||Analysis of reviews and ratings of the establishment|
|**8**|**:money_with_wings:Payment system**|**View payment history and fees**|
|8.1||Setting up payment methods (to a bank account or card)|
|8.2||Possibility to receive a financial report|
|**9**|**👩‍💻Human Resources Management**|**Adding employees with different access levels**|
|9.1||Assigning roles and rights (access to menus, orders or reports)|
|**10**|**:mailbox_closed:Setting up the establishment's work**|**Possibility of temporary shutdown of order acceptance (for technical break)**|
|**11**|**:speech_balloon:Technical support**|**Access to support chat for technical issues**|

### Courier functionality
|1|:memo:Registration and authorization|Possibility of registration via phone number|   
|:-|:-|:-|
|1.1||Account verification via moderation (data verification)|
|**2**|**:rice_scene:Courier profile**|**Indication of the type of transport (pedestrian, bicycle, car, motorcycle)**|
|2.1||Ability to upload a profile photo|
|**3**|**:ledger:Sending order information**|**The bot sends the courier a notification about a new order with details (restaurant address, delivery address, list of dishes, special customer requests)**|
|3.1||The courier can accept or refuse the order|
|3.2||The rejected order will be offered to another courier|
|**4**|**:arrow_up_small:Order status tracking**|**The bot sends notifications to the courier about the change in the order status (the order is ready for pickup, in progress)**|
|4.1||The courier can mark the stages of order fulfillment (order in transit, order delivered)|
|**5**|**:phone:Interaction with the client**|**БThe bot gives the courier the opportunity to contact the client via chat or call.**|
|5.1||The bot sends notifications to the courier about problems with the order (the delivery address is incorrect)|
|**6**|**:star:Rating and reviews**|**The bot sends notifications to the courier about new reviews and ratings.**|
|**7**|**:clock10:Working hours**|**The bot sends notifications to the courier about the start and end of the working day**|
|7.1||The courier can temporarily disable accepting orders via the bot|
|**8**|**:speech_balloon:Technical support**|**The bot provides the courier with access to the support chat to resolve technical issues**|
|8.1||The bot informs the courier about technical updates and innovations|

## :wrench:Build & Deploy
#### Technical requirements for the system on which the project is launched:
- Docker must be installed

#### Start project locally
You need to run command below in your terminal:
```
docker compose -f USER/delivery-service/docker/docker-compose.yaml -p food-delivery-bot up -d
```
Change params **USER** to the path of your project and **TELEGRAM_BOT_TOKEN** to token of your bot.


## :books:Documentation
- [Task tracker](https://tracker.yandex.ru/DELIVERYCLUB)
- [Technical task](https://docs.google.com/document/d/1beqlwbVsplGTg-OuFVpLeMwRE0NW_g-V/edit?usp=sharing&ouid=115171977064742232207&rtpof=true&sd=true) 

## :busts_in_silhouette:Development Team
- Ibragimov Timur, developer-team lead
- Satkanov Altnbek, developer
- Suleymanov Ilnur, systems analyst
- Gilfanova Diana, developer

## :eyes:Sponsors
[IITIS KFU](https://kpfu.ru/itis)
