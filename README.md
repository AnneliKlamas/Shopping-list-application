# Shopping-list-application

Shopping list application is an application where user can add items and amount to the list. Item and amount are both string, because I thought that it is user friendlier to write "1 ts" to one text field than writing "1" to one and "ts" to other text field. User can also delete checked items or all items. If user tries to add item that already exists in the list, he will get a pop-up window with question does the user want to replace current item or keep it. If user choose to replace it by accident, then he can go back with back button and change his choice. It also remembers user's checked boxes when he closes application, but user has to click sometimes on "refresh" button for table to appear. I didn't think a lot about colors of the application, so I took Android Studio's primary colors which might be too bright. 

There are 2 classes (Activities): one for list and one for pop-up window. And 2 Layouts for each class. There is also drawable xml file for both activity backrounds.

This application doesen't have any automated tests, because I couldn't figure out how to make them...


How to run application: You can run application in Your IDE with emulator or phone(make sure You have enabled debbuging in developer mode), which is connected by USB. You can also download application from this link: https://mega.nz/file/bgsGBCgR#Hxb2Aog_UM3emkiGVMoQ2c9vqyQUD2RfVyv_UP_4Zbg
