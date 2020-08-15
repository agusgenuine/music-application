import pyrebase

class Connecting():

    def __init__(self):
        config = {
            "apiKey": "AIzaSyCTz54-PZRqYyEIBT_FDV-cyPWr_Y95klE",
            "authDomain": "music-player-d02e5.firebaseapp.com",
            "databaseURL": "https://music-player-d02e5.firebaseio.com",
            "projectId": "music-player-d02e5",
            "storageBucket": "music-player-d02e5.appspot.com",
            "messagingSenderId": "322007107163",
            "appId": "1:322007107163:web:43f44dddb3c9ad9a34e17e",
            "measurementId": "G-68ZTGJNKTW"
        }
        self.firebase = pyrebase.initialize_app(config)

    def getFirebase(self):
        return self.firebase

    def getFireStorage(self):
        return self.firebase.storage()

    def getDatabase(self):
        return self.firebase.database()
