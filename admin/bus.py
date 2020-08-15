import dto
import eyed3
import model
import utils
import pyrebase

class SongDAO():
    '''
    class bus for Song object
    '''
    def __init__(self):
        DTO = dto.Connecting()
        self.firebase = DTO.getFirebase()
        self.storage = DTO.getFireStorage()
        self.database = DTO.getDatabase()


    def uploadFile(self, local_path, cloud_path, name_file):
        '''
        upload audio file to firestorage
        function return real path in firebase (path to play this song)
        '''
        url = cloud_path+'/' + name_file
        self.storage.child(url).put(local_path)
        return self.storage.child(url).get_url(None)


    def detectData(self, local_path):
        '''
        read data from file mp3
        upload file into firebase
        funtion return song model
        '''
        file = eyed3.load(local_path)
        tag = file.tag

        resp_url = self.uploadFile(local_path, 'songs', tag.title+'.mp3')

        song = model.Song(utils.generateID(), tag.title, tag.artist, tag.album, utils.getNow(), 0, 'No Lyris', resp_url)
        return song


    def addSong(self, local_path):
        '''
        upload song data to firebase database
        '''
        try:
            song = self.detectData(local_path)
            #insert to database
            data =  {
                "title": song.name,
                "artist": song.artist,
                "album": song.album,
                "date_created": song.date_created,
                "views": song.views,
                "lyric": song.lyric,
                "data": song.data
            }
            self.database.child('music-player/songs').child(song.sid).set(data)
            return True
        except:
            #print("can't add this song to database!")
            return False


    def updateSong(self, song):
        '''
        update song
        '''
        try:
            self.database.child("music-player/songs").child(song.sid).update({
                "title": song.name,
                "artist": song.artist,
                "album": song.album,
                "date_created": song.date_created,
                "views": song.views,
                "lyric": song.lyric,
            })
            return True
        except:
            return False


    def deleteSong(self, song_id, name_file):
        '''
        delete song on database and storage
        song_id use to delete on database
        name_file (song title + .mp3) use to delete file on firestorage
        '''
        try:
            self.database.child('music-player/songs').child(song_id).remove()
            self.storage.child('songs').delete(name_file)
            return True
        except:
            return False


    def getSongDetails(self, id):
        '''
        function get detail of song with id input
        if id invalid return None
        '''
        try:
            dic = self.database.child('music-player/songs').child(id).get().val()
            song = model.Song(id, dic["title"], dic["artist"], dic["album"], dic["date_created"], dic["views"], dic["lyric"], dic["data"])
            return song
        except:
            return None


    def readData(self):
        '''
        function return list id of songs
        '''
        try:
            songs = self.database.child('music-player/songs').get()
            return list(songs.val())
        except:
            return None


class ArtistDAO():
    '''
    class bus for Artist object
    '''
    def __init__(self):
        DTO = dto.Connecting()
        self.firebase = DTO.getFirebase()
        self.storage = DTO.getFireStorage()
        self.database = DTO.getDatabase()

    def uploadFile(self, local_path, cloud_path, name_file):
        '''
        upload file to firestorage
        function return real path in firebase (path to play this song)
        '''
        url = cloud_path+'/' + name_file
        self.storage.child(url).put(local_path)
        return self.storage.child(url).get_url(None)

    def addArtist(self, artist):
        '''
        upload artist data to firebase database
        '''
        try:
            if artist.cover != None or artist != "":
                resp_url = self.uploadFile(artist.cover, 'artists', artist.name+'.png')
            #insert to database
            data =  {
                "name": artist.name,
                "cover": resp_url,
                "profile": artist.profile
            }
            self.database.child('music-player/artists').child(artist.aid).set(data)
            return True
        except:
            return False

    def updateArtist(self, artist):
        '''
        update artist
        '''
        try:
            self.database.child("music-player/artists").child(artist.aid).update({
                "name": artist.name,
                "cover": artist.cover,
                "profile": artist.profile
            })
            return True
        except:
            return False

    def deleteArtist(self, artist_id, name_file):
        '''
        delete artist on database and storage
        artist_id use to delete on database
        name_file (artist name + .png) use to delete file on firestorage
        '''
        try:
            self.database.child('music-player/artists').child(artist_id).remove()
            self.storage.child('artists').delete(name_file)
            return True
        except:
            return False

    def getArtistDetails(self, artist_id):
        '''
        function get detail of artist with id input
        if id invalid return None
        '''
        try:
            dic = self.database.child('music-player/artists').child(artist_id).get().val()
            artist = model.Artist(artist_id, dic["name"], dic["cover"], dic["profile"])
            return artist
        except:
            return None

    def readData(self):
        '''
        function return list id of songs
        '''
        try:
            artists = self.database.child('music-player/artists').get()
            return list(artists.val())
        except:
            return None


class CollectionDAO():
    '''
    class bus for Collection object
    '''
    def __init__(self):
        DTO = dto.Connecting()
        self.firebase = DTO.getFirebase()
        self.storage = DTO.getFireStorage()
        self.database = DTO.getDatabase()

    def addCollection(self, collection):
        '''
        upload collection data to firebase database
        '''
        try:
            #insert to database
            data =  {
                "name": collection.name,
                "songs": collection.listSong
            }
            self.database.child('music-player/collections').child(collection.cid).set(data)
            return True
        except:
            return False

    def updateCollection(self, collection):
        '''
        update artist
        '''
        try:
            self.database.child("music-player/collections").child(collection.cid).update({
                "name": collection.name,
                "songs": collection.listSong
            })
            return True
        except:
            return False

    def deleteCollection(self, collec_id):
        '''
        delete collection on database and storage
        collec_id use to delete on database
        '''
        try:
            self.database.child('music-player/collections').child(collec_id).remove()
            return True
        except:
            return False

    def getCollectionDetails(self, collec_id):
        '''
        function get detail of collection with id input
        if id invalid return None
        '''
        try:
            dic = self.database.child('music-player/collections').child(collec_id).get().val()
            collec = model.Collection(collec_id, dic["name"], dic["songs"])
            return collec
        except: 
            return None

    def readData(self):
        '''
        function return list id of collection
        '''
        try:
            collec = self.database.child('music-player/collections').get()
            return list(collec.val())
        except:
            return None