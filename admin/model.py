class Song():
    def __init__(self, sid=0, name=None, artist=None, album=None, date_created=None, views=0, lyric=None, data=None):
        self.sid = sid
        self.name = name
        self.artist = artist
        self.album = album
        self.date_created = date_created
        self.views = views
        self.lyric = lyric
        self.data = data
    
    def display(self):
        print('-------------')
        print('ID:', self.sid)
        print('Title:', self.name)
        print('Artist:', self.artist)
        print('Album', self.album)
        print('Date Created:', self.date_created)
        print('Views:', self.views)
        print('Lyric:', self.lyric)
        print('Data:', self.data)
        print('-------------')

class Artist():
    def __init__(self, aid=0, name=None, cover=None, profile=None):
        self.aid = aid
        self.name = name
        self.cover = cover
        self.profile = profile

    def display(self):
        print('-------------')
        print('ID:', self.aid)
        print('Title:', self.name)
        print('Cover Image:', self.cover)
        print('Profile', self.profile)
        print('-------------')

class Collection():
    def __init__(self, cid=0, name=None, listSong=[]):
        self.cid = cid
        self.name = name
        self.listSong = list(listSong)

    def display(self):
        print('-------------')
        print('ID:', self.cid)
        print('Title:', self.name)
        print('+ List song:')
        for song in self.listSong:
            print('\t-',"Id:", song)
        print('-------------')