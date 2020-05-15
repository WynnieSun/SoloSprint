# CSC360 SoloSprint - WynnieSun

### Proposed 3 tasks:
1. compare: a listview on GUI shows all different sections between two BPs
2. comment: a listview on GUI shows all comments for each selected section
3. subscription: clients observe server, users observe BPs; both logging and simultaneous notification for observers 

### There are 6 test files under test/views package   

Useful tests for sprint5:   
1. TestCompareView: a functional and unit test for task 1     
2. TestSectionView: a functional and unit test for task 2     
3. TestNotificationSubscriptionView: a functional and unit test for task 3   
4. TestMainPageView: a functional test for the whole program    

Unnecessary tests for sprint4, no need to see:    
1. TestEditingView   
2. TestBPMainView  

### NOTE:
For task 3, I have added an external jar called controlsfx. I'm not sure if the build path will work after you download this repository.

Also, For task 3, beside the logging system, I added a popup notification on top left that reminds users their followBP has been changed by others. Even if the BP is changed by the current user on Client, the user will also get that notification as long as he has followed the BP. The popup notification on top right is not important. 
