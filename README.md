# Braid
## User Interface
The main interface resembles a catalogue where the users are able to add new books and interact for books they have entered. Users are able to select the floating action button which expands to give three different options of adding books. They are able to scan the barcode, manually enter the ISBN or manually enter all of the book details. In figure 2 below are some images of the user interface. 

## Interactions/ User Guide: 
### Installation and Running
- Open the project using Android Studio
- Click “Run app”
- Select an emulator and click OK. 
- An emulator will open on screen for the user
- The application will then launch

### Operating the Application
Once the application launches after a fresh install, the database will be clear for the user to then add in their books. The Floating Action Bar (FAB) expands to give the user multiple ways to add a book. Clicking the FAB will expand the view to show three options, all self explanatory. 

The add manually option allows the user to add a custom book to the application. They can input a wide range of details which they can then save for later viewing or editing. 

The add by ISBN option gives a user an input dialog for an ISBN which will then be parsed to extract the relevant book details. If details are found, it pre populates the fields and it is up to the user to save or discard the data. If no data is found for the ISBN, an error toast is shown and the user is prompted to enter details manually

The add by camera option lets the user scan a barcode and use that to prepopulate the fields. Similar to the add by ISBN option, the user can save or discard the details.

The main view shows the currently stored books as a list. Users are able to scroll through the list and interact with each item through the option menu on the top right. 

The tabbed display shows three headings, All books, Read Books and Unread Books. By swiping through them or tapping the headings, a different list of books are shown. All books show all the books within the database. The other two options display books based on their calculated total and current page values. Read Books show books where current page = total pages meaning the user had completed the book. Unread Books show books that have current page < total page meaning they have not yet completed the book. 

## Discussion of Solution

The solution we developed is a modular and robust application which can easily be extended for the future. We kept the design intuitive and standard against the android material guidelines. The use of fragments was a heavy influence in the implementation of the application as this is a mobile application which will run on multiple different devices all with different levels of functionality. 

The main idea was to follow the Model View Controller design pattern to create loosely coupled references to other fragments, each fragment can easily be adapted to suit their own specific functionality without the dependency of other fragments. 

Retaining and error checking data throughout the application lifecycle was important to us as we knew users would want an experience where they can easily add and view their books without any issues or data loss. When the user changes orientation, all the data is kept and repopulated. Users are unable to input any incorrect fields as the error checking was real time such that any changes would be reflected on all fields. 

Our implementation included the usage of a library for barcode scanning called ZXing. ZXing allowed us to easily extract data from a barcode, namely the ISBN. Other code extracts were used to help understand the implementation if that functionality. An example of this was the Volley class which allows HTTP requests asynchronously. Although we used external libraries and functions credit was given to the original authors. 

Overall, the design and implementation took the form of a prototyping methodology. We developed an initial version and then expanded on it incrementally throughout the project lifecycle. GitHub was used to collaborate between each other.

## Database: 
Our database consists of 4 tables. A “Book” table stores data about each book added such as its title and genre. Secondly an “Author” table stores information about each author. Due to the many-to-many relationship nature of these two tables, the association table “BookAuthor” is required. Finally the publisher is given it’s own table since each publisher will publish multiple books. We designed our database with future extensions to the application in mind, like the ability to search for keywords and view all of a specified author's work. 


## Additional features: 

As the user is able to add information about a book it is fitting that they would also be able to add an image. For our application we gave the user the ability to use the camera to take a picture. The user will not have to leave the application in order to take the picture. The image is stored internally within the application storage and referenced by the SQLite database. 

An extension to this feature is using the ZXING library to use the user's camera to scan for a barcode to extract and search for the details specific to that ISBN. It uses the google books API to extract and populate the book information. All of this is done asynchronously so that the user experience is not hindered in any way. The camera permission must be granted for this feature

By using the Google Books api to fetch details about books, users are able to enter their desired books faster and easily. An internet connection is required for this feature to work. 

Simple user experience improvements are included such as a slider to indicate the current page based on the total pages as well as error checking to ensure the user does not accidentally enter invalid details
