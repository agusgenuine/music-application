import os
import glob
from bus import *
from model import *
from utils import *

def main(): 
    options = ["\t1. Songs\t\t", "\t2. Artists\t\t", "\t3. Collections\t\t", "\t4. Export Data\t\t", "\t0. Exit\t\t\t" ]
    choose = -1
    cls()
    while(True):   
        print("=============MANAGERS=============")
        for option in options:
            print(chr(19),option,chr(19))
        print("==================================")

        choose = int(input("Choose Your Option: "))
        cls()
        if choose == 1:
            song_manager()
        elif choose == 2:
            artist_manager()
        elif choose == 3:
            collection_manager()
        elif choose == 4:
            export_data()
        elif choose == 0:
            break

def song_manager():
    sDAO = SongDAO()
    choose = -1
    while(True):   
        print("===========SONG MANAGER===========")
        
        songIds = sDAO.getList()
        print('0. Back\t',len(songIds)+1,'. Create New')        #Back or Create

        #List Song
        for index in range(0, len(songIds)-1):
            print(index+1,'.',sDAO.getSongDetails(songIds[index]).name, end='\t\t')
            if index % 3 == 0:
                print()

        choose = int(input("Song Index: "))
        cls()

        if choose == 0:
            break

        #Display Song Details
        elif 0 < choose <= len(songIds):
            while True:
                print("0. Back\t1. Edit\t2. Delete")
                print("===========SONG DETAILS===========")
                song = sDAO.getSongDetails(choose-1)
                song.display()
                choose = int(input('Action: '))
                cls()
                if choose == 0:
                    break
                elif choose == 1:
                    #TODO
                    #EDIT SONG HERE
                    pass
                elif choose == 2:
                    confirm = input('Are you sure (Y/N)? ')
                    if confirm == 'y' or confirm =='Y':
                        #WARING
                        #Delete file in fire storage not working
                        sDAO.deleteSong(song.sid)
                        cls()
                        break
                    else:
                        cls()

        #Create New Song
        elif choose == len(songIds)+1:
            while True:
                print("===========SONG UPLOAD============")
                print("Upload from file:   C:\Folder_Data\Song.mp3")
                print("Upload from folder: C:\Folder_Data")
                path = input("Your Path: ")
                #Uplaod 1
                if path.endswith("*.mp3"):
                    if sDAO.addSong(path):
                        print("Upload Successful!")
                    else:
                        print("Upload Fails!")
                #Upload more than 1
                else:
                    song_paths = [f for f in glob.glob(path + "**/*.mp3", recursive=True)]
                    if len(song_paths) == 0:
                        print("At Least 1 file .txt")
                    else:
                        for song_path in song_paths:
                            if sDAO.addSong(song_path):
                                cut = song_path.split('\\')
                                print(cut[len(cut)-1], 'Successful!')
                            else:
                                cut = song_path.split('\\')
                                print(cut[len(cut)-1], 'Fails!')
                        print('Done!')

                #Continue Uplaod?
                choose = input('Continue (Y/N)? ')
                if choose == 'n' or choose == 'N':
                    cls()
                    break
                else:
                    cls()
            
def artist_manager():
    aDAO = ArtistDAO()
    choose = -1
    while(True):   
        print("==========ARTIST MANAGER==========")
        
        artistIds = aDAO.getList()
        print('0. Back\t',len(artistIds)+1,'. Create New')        #Back or Create

        #List Artist
        for index in range(0, len(artistIds)-1):
            print(index+1,'.',aDAO.getArtistDetails(artistIds[index]).name, end='\t\t')
            if index % 3 == 0:
                print()

        choose = int(input("Artist Index: "))
        cls()

        if choose == 0:
            break

        #Display Artist Details
        elif 0 < choose <= len(artistIds):
            while True:
                print("0. Back\t1. Edit\t2. Delete")
                print("==========ARTIST DETAILS==========")
                artist = aDAO.getArtistDetails(choose-1)
                artist.display()
                choose = int(input('Action: '))
                cls()
                if choose == 0:
                    break
                elif choose == 1:
                    #TODO
                    #EDIT ARTIST HERE
                    pass
                elif choose == 2:
                    confirm = input('Are you sure (Y/N)? ')
                    if confirm == 'y' or confirm =='Y':
                        #WARING
                        #Delete file in fire storage not working
                        aDAO.deleteArtist(artist.aid)
                        cls()
                        break
                    else:
                        cls()

        #Create New ARTIST
        elif choose == len(artistIds)+1:
            while True:
                intro = 'FILE PATTERN:\n\
                Name of artist          <= Line 1\n\
                C:\Image\Artist.png     <= Second line: Image cover local path\n\
                Multiline after         <= Profile of Artist (Same as: Name, age, address, ...)\n'
                print(intro)

                print("==========ARTIST UPLOAD===========")
                print("Upload from file:   C:\Folder_Data\Artist.txt")
                print("Upload from folder: C:\Folder_Data")     #Folder Contain .txt Files
                path = input("Your Path: ")
                #Uplaod 1
                if path.endswith("*.txt"):
                    if aDAO.addArtist(path):
                        print("Upload Successful!")
                    else:
                        print("Upload Fails!")
                #Upload more than 1
                else:
                    artist_paths = [f for f in glob.glob(path + "**/*.txt", recursive=True)]
                    if len(artist_paths) == 0:
                        print("Folder must be at least 1 file .txt :((")
                    else:
                        for artist_path in artist_paths:
                            if aDAO.addArtist(artist_path):
                                cut = artist_path.split('\\')
                                print(cut[len(cut)-1], 'Successful!')
                            else:
                                cut = artist_path.split('\\')
                                print(cut[len(cut)-1], 'Fails!')
                        print('Done!')

                #Continue Uplaod?
                choose = input('Continue (Y/N)? ')
                if choose == 'n' or choose == 'N':
                    cls()
                    break
                else:
                    cls()

def collection_manager():
    cDAO = CollectionDAO()
    choose = -1
    while(True):   
        print("========COLLECTION MANAGER========")
        
        collecIds = cDAO.getList()
        print('0. Back\t',len(collecIds)+1,'. Create New')        #Back or Create

        #List Collection
        for index in range(0, len(collecIds)-1):
            print(index+1,'.',cDAO.getCollectionDetails(collecIds[index]).name, end='\t\t')
            if index % 3 == 0:
                print()

        choose = int(input("Collection Index: "))
        cls()

        if choose == 0:
            break

        #Display Collection Details
        elif 0 < choose <= len(collecIds):
            while True:
                print("0. Back\t1. Edit\t2. Delete")
                print("========COLLECTION DETAILS========")
                collection = cDAO.getCollectionDetails(choose-1)
                collection.display()
                choose = int(input('Action: '))
                cls()
                if choose == 0:
                    break
                elif choose == 1:
                    #TODO
                    #EDIT COLLECTION HERE
                    pass
                elif choose == 2:
                    confirm = input('Are you sure (Y/N)? ')
                    if confirm == 'y' or confirm =='Y':
                        cDAO.deleteCollection(collection.cid)
                        cls()
                        break
                    else:
                        cls()

        #Create New Collection
        elif choose == len(collecIds)+1:
            while True:
                intro = 'FILE PATTERN:\n\
                Name of collection      <= Line 1\n\
                SongId1 SongId2 SongId3 <= Line 2 (remember space between Ids)\n'
                print(intro)                
                print("========COLLECTION UPLOAD=========")
                print("Upload from file:   C:\Folder_Data\Collection.txt")
                print("Upload from folder: C:\Folder_Data")     #Contain .txt files
                path = input("Your Path: ")
                #Uplaod 1
                if path.endswith("*.txt"):
                    if cDAO.addCollection(path):
                        print("Upload Successful!")
                    else:
                        print("Upload Fails!")
                #Upload more than 1
                else:
                    collec_paths = [f for f in glob.glob(path + "**/*.txt", recursive=True)]
                    if len(collec_paths) == 0:
                        print("Folder must be at least 1 file .txt :((")
                    else:
                        for collec_path in collec_paths:
                            if cDAO.addCollection(collec_path):
                                cut = collec_path.split('\\')
                                print(cut[len(cut)-1], 'Successful!')
                            else:
                                cut = collec_path.split('\\')
                                print(cut[len(cut)-1], 'Fails!')
                        print('Done!')

                #Continue Uplaod?
                choose = input('Continue (Y/N)? ')
                if choose == 'n' or choose == 'N':
                    cls()
                    break
                else:
                    cls()

def export_data():
    sDAO = SongDAO()
    aDAO = ArtistDAO()
    cDAO = CollectionDAO()

    path = str(os.getcwd()) + '\\DATA_EXPORT'

    #Create General Folder
    try:
        os.mkdir(path, 0o755)
    except:
        pass

    #Create Song Folder
    song_path = path + '\\SONGS'
    try:
        os.mkdir(song_path, 0o755)
    except:
        pass

    songIds = sDAO.getList()
    for id in songIds:
        song = sDAO.getSongDetails(id)
        with open(song_path+song.sid+'.txt', 'w+') as _file:
            _file.write(song.sid)
            _file.write(song.name)
            _file.write(song.artist)
            _file.write(song.album)
            _file.write(song.date_created)
            _file.write(song.views)
            _file.writes(song.lyric)
            _file.write(song.data)
    print('Write songs success')

    #Create Artist Folder
    artist_path = path + '\\ARTISTS'
    try:
        os.mkdir(artist_path, 0o755)
    except:
        pass

    artistIds = aDAO.getList()
    for id in artistIds:
        artist = aDAO.getArtistDetails(id)
        with open(song_path+artist.aid+'.txt', 'w+') as _file:
            _file.write(artist.name)
            _file.write(artist.cover)
            _file.writes(artist.profile)
    print('Write artists success')

    #Create Collection Folder
    collec_path = path + '\\COLLECTIONS'
    try:
        os.mkdir(collec_path, 0o755)
    except:
        pass

    collecIds = cDAO.getList()
    for id in collecIds:
        collec = cDAO.getCollectionDetails(id)
        with open(collec_path+collec.cid+'.txt', 'w+') as _file:
            _file.write(collec.name)
            _file.write(collec.listSong)
    print('Write collections success')

    print('Export Successful!')
    print('Saving at:',path)
    input()
    cls()

if __name__ == "__main__":
    main()