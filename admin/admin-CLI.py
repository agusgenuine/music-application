import os
import glob
from bus import *
from model import *
from utils import *

def main(): 
    options = ["\t1. View Data\t\t", "\t2. New Data\t\t", "\t3. Edit Data\t\t", "\t4. Delete Data\t\t", "\t0. Exit\t\t\t" ]
    choose = -1
    cls()
    while(True):   
        print("=============OPTIONS==============")
        for option in options:
            print(chr(19),option,chr(19))
        print("==================================")

        choose = int(input("Choose Your Option: "))
        cls()
        if choose == 1:
            viewData()
        elif choose == 2:
            uploadData()
        elif choose == 3:
            editData()
        elif choose == 4:
            deleteData()
        elif choose == 0:
            break
        else:
            print('Option not found (⊙_⊙;)')
            print('Please try again!')


def viewData():
    options = ["\t1. View Songs\t\t", "\t2. View Artists\t\t", "\t3. View Collections\t", "\t0. Back\t\t\t" ]
    choose = -1
    while(True):   
        print("=============VIEW DATA============")
        for option in options:
            print(chr(19),option,chr(19))
        print("==================================")

        choose = int(input("Choose Your Option: "))
        cls()
        if choose == 1:
            viewSongs()
        elif choose == 2:
            viewArtists()
        elif choose == 3:
            viewCollections()
        elif choose == 0:
            break
        else:
            print('Option not found (⊙_⊙;)')
            print('Please try again!')
def viewSongs():
    song_DAO = SongDAO()
    while True:
        listId = song_DAO.readData()
        if listId == None:
            print('No Data! Plz add new song!')
            choose = int(input("Press 0 To Back: "))
        else:
            print('0. Back')
            for i in range(len(listId)):
                print(i+1,'.', song_DAO.getSongDetails(listId[i]).name)
            choose = int(input("More Details: "))
        cls()
        if 0 < choose <= len(listId):
            while True:
                song = song_DAO.getSongDetails(listId[choose-1])
                song.display()
                options = ["\t1. Edit Song\t\t", "\t2. Delete Song\t\t", "\t0. Back\t\t\t" ]
                print("==============OPTIONS=============")
                for option in options:
                    print(chr(19),option,chr(19))
                print("==================================")
                op = int(input("Your Option: "))
                cls()
                if op == 0:
                    break
                if op == 1:
                    if editSong(song.sid):
                        print('Edit Successful!')
                    else:
                        print('Edit Fail!')
                elif op == 2:
                    if deleteSong(song.sid):
                        break
                    else:
                        print('Delete Fail!')
                        break
        elif choose == 0:
            break
        else:
            print('Index out of range!!!')
def viewArtists():
    artist_DAO = ArtistDAO()
    while True:
        listId = artist_DAO.readData()
        if listId == None:
            print('No Data! Plz add new artist!')
            choose = int(input("Press 0 To Back: "))
        else:
            print('0. Back')
            for i in range(len(listId)):
                print(i+1,'.', artist_DAO.getArtistDetails(listId[i]).name)
            choose = int(input("More Details: "))
        cls()
        if 0 < choose <= len(listId):
            while True:
                artist = artist_DAO.getArtistDetails(listId[choose-1])
                artist.display()
                options = ["\t1. Edit Artist\t\t", "\t2. Delete Artist\t", "\t0. Back\t\t\t" ]
                print("==============OPTIONS=============")
                for option in options:
                    print(chr(19),option,chr(19))
                print("==================================")
                op = int(input("Your Option: "))
                cls()
                if op == 0:
                    break
                if op == 1:
                    if editArtist(artist.aid):
                        print('Edit Successful!')
                    else:
                        print('Edit Fail!')
                elif op == 2:
                    if deleteArtist(artist.aid):
                        break
                    else:
                        print('Delete Fail!')
                        break
        elif choose == 0:
            break
        else:
            print('Index out of range!!!')
def viewCollections():
    collec_DAO = CollectionDAO()
    while True:
        listId = collec_DAO.readData()
        if listId == None:
            print('No Data! Plz add new collection!')
            choose = int(input("Press 0 To Back: "))
        else:
            print('0. Back')
            for i in range(len(listId)):
                print(i+1,'.', collec_DAO.getCollectionDetails(listId[i]).name)
            choose = int(input("More Details: "))
        cls()
        if 0 < choose <= len(listId):
            while True:
                collec = collec_DAO.getCollectionDetails(listId[choose-1])
                collec.display()
                options = ["\t1. Edit Collection\t", "\t2. Delete Collection\t", "\t0. Back\t\t\t" ]
                print("==============OPTIONS=============")
                for option in options:
                    print(chr(19),option,chr(19))
                print("==================================")
                op = int(input("Your Option: "))
                cls()
                if op == 0:
                    break
                if op == 1:
                    if editCollection(collec.cid):
                        print('Edit Successful!')
                    else:
                        print('Edit Fail!')
                elif op == 2:
                    if deleteCollection(collec.cid):
                        break
                    else:
                        print('Delete Fail!')
                        break
        elif choose == 0:
            break
        else:
            print('Index out of range!!!')


def uploadData():
    options = ["\t1. New Songs\t\t", "\t2. New Artists\t\t", "\t3. New Collections\t", "\t0. Back\t\t\t" ]
    choose = -1
    while(True):   
        print("=============NEW DATA=============")
        for option in options:
            print(chr(19),option,chr(19))
        print("==================================")

        choose = int(input("Choose Your Option: "))
        cls()
        if choose == 1:
            uploadSong()
        elif choose == 2:
            uploadArtist()
        elif choose == 3:
            uploadCollection()
        elif choose == 0:
            break
        else:
            print('Option not found (⊙_⊙;)')
            print('Please try again!')
def uploadSong():
    options = ["\t1. From File\t\t", "\t2. From Folder\t\t", "\t0. Back\t\t\t" ]
    choose = -1
    cont = 'n'
    while True:
        while True:   
            print("==============NEW SONG============")
            for option in options:
                print(chr(19),option,chr(19))
            print("==================================")

            choose = int(input("Choose Your Option: "))
            cls()
            song_DAO = SongDAO()
            if choose == 1:
                path = input("Enter Your File Path (Example: C:\\Datas\\Songs\\new.mp3): ")
                result = song_DAO.addSong(path)
                file_name = path.split('\\')
                if result:
                    print(file_name[len(file_name)-1], 'Upload Success')
                else:
                    print(file_name[len(file_name)-1], 'Upload Fail')
                break
            elif choose == 2:
                path = input("Enter Your Folder Path (Example: C:\\Foler_Data): ")
                songs = [f for f in glob.glob(path + "**/*.mp3", recursive=True)]
                for song in songs:
                    result = song_DAO.addSong(song)
                    if result:
                        file_name = song.split('\\')
                        print(file_name[len(file_name)-1], 'Success')
                    else:
                        print(file_name[len(file_name)-1], 'Fail')
                break
            elif choose == 0:
                break
            else:
                print('Option not found (⊙_⊙;)')
                print('Please try again!')
        cont = input('Continue? (Y/N): ')
        if cont == 'N' or cont == 'n':
            break
def uploadArtist():
    options = ["\t1. From File\t\t", "\t2. Manual\t\t", "\t0. Back\t\t\t" ]
    choose = -1
    cont = 'n'
    while True:
        while True:   
            print("=============NEW ARTIST===========")
            for option in options:
                print(chr(19),option,chr(19))
            print("==================================")

            choose = int(input("Choose Your Option: "))
            cls()
            artist_DAO = ArtistDAO()
            if choose == 1:
                path = input("Enter Your File Path (Example: C:\\Datas\\Songs\\input.txt): ")
                #TODO
                #artist = ReadFromFile
                #result = artist_DAO.addArtist(path)
                # file_name = path.split('\\')
                # if result:
                #     print(file_name[len(file_name)-1], 'Upload Success')
                # else:
                #     print(file_name[len(file_name)-1], 'Upload Fail')
                break
            elif choose == 2:
                #TODO
                #FOR atrribute in line: input line by line
                # path = input("Enter Your Folder Path (Example: C:\\Foler_Data): ")
                # songs = [f for f in glob.glob(path + "**/*.mp3", recursive=True)]
                # for song in songs:
                #     result = song_DAO.addSong(song)
                #     if result:
                #         file_name = song.split('\\')
                #         print(file_name[len(file_name)-1], 'Success')
                #     else:
                #         print(file_name[len(file_name)-1], 'Fail')
                break
            elif choose == 0:
                break
            else:
                print('Option not found (⊙_⊙;)')
                print('Please try again!')
        cont = input('Continue? (Y/N): ')
        if cont == 'N' or cont == 'n':
            break
def uploadCollection():
    options = ["\t1. From File\t\t", "\t2. Manual\t\t", "\t0. Back\t\t\t" ]
    choose = -1
    cont = 'n'
    while True:
        while True:   
            print("===========NEW COLLECTION=========")
            for option in options:
                print(chr(19),option,chr(19))
            print("==================================")

            choose = int(input("Choose Your Option: "))
            cls()
            collec_DAO = CollectionDAO()
            if choose == 1:
                path = input("Enter Your File Path (Example: C:\\Datas\\Songs\\input.txt): ")
                #TODO
                #collec = ReadFromFile
                #result = artist_DAO.addArtist(path)
                # file_name = path.split('\\')
                # if result:
                #     print(file_name[len(file_name)-1], 'Upload Success')
                # else:
                #     print(file_name[len(file_name)-1], 'Upload Fail')
                break
            elif choose == 2:
                #TODO
                #FOR atrribute in line: input line by line
                # path = input("Enter Your Folder Path (Example: C:\\Foler_Data): ")
                # songs = [f for f in glob.glob(path + "**/*.mp3", recursive=True)]
                # for song in songs:
                #     result = song_DAO.addSong(song)
                #     if result:
                #         file_name = song.split('\\')
                #         print(file_name[len(file_name)-1], 'Success')
                #     else:
                #         print(file_name[len(file_name)-1], 'Fail')
                break
            elif choose == 0:
                break
            else:
                print('Option not found (⊙_⊙;)')
                print('Please try again!')
        cont = input('Continue? (Y/N): ')
        if cont == 'N' or cont == 'n':
            break


def editData():
    options = ["\t1. Edit Songs\t\t", "\t2. Edit Artists\t\t", "\t3. Edit Collections\t", "\t0. Back\t\t\t" ]
    choose = -1
    while(True):   
        print("=============EDIT DATA============")
        for option in options:
            print(chr(19),option,chr(19))
        print("==================================")

        choose = int(input("Choose Your Option: "))
        cls()
        if choose == 1:
            if editSong():
                print('Edit Success!')
            else:
                print('Edit Fail!')
        elif choose == 2:
            if editArtist():
                print('Edit Success!')
            else:
                print('Edit Fail!')
        elif choose == 3:
            if editCollection():
                print('Edit Success!')
            else:
                print('Edit Fail!')
        elif choose == 0:
            break
        else:
            print('Option not found (⊙_⊙;)')
            print('Please try again!')
def editSong(id=None):
    while id == None or id == '':
        id = input('Enter Song Id: ')
    song_DAO = SongDAO()
    song = song_DAO.getSongDetails(id)
    if song == None:
        return False
    new_name = input('New Name (Empty If No Change): ')
    if new_name != '':
        song.name = new_name
    new_artist = input('New Artist (Empty If No Change): ')
    if new_artist != '':
        song.artist = new_artist
    new_album = input('New Album (Empty If No Change): ')
    if new_album != '':
        song.album = new_album
    new_views = input('New Views (Empty If No Change): ')
    if new_views != '':
        song.views = new_views
    new_lyric = input('New Lyric (Empty If No Change): ')
    if new_lyric != '':
        song.lyric = new_lyric
    cls()
    song.display()
    confirm = input('Apply Change (Y/N): ')
    if confirm == 'y' or confirm == 'Y':
        return song_DAO.updateSong(song)
def editArtist(id=None):
    while id == None or id == '':
        id = input('Enter Artist Id: ')
    artist_DAO = ArtistDAO()
    artist = artist_DAO.getArtistDetails(id)
    if artist == None:
        return False
    new_name = input('New Name (Empty If No Change): ')
    if new_name != '':
        artist.name = new_name
    cover = input('New Cover Image Link (Empty If No Change): ')
    if cover != '':
        artist.cover = cover
    profile = input('New Profile (Empty If No Change): ')
    if profile != '':
        artist.profile = profile
    cls()
    artist.display()
    confirm = input('Apply Change (Y/N): ')
    if confirm == 'y' or confirm == 'Y':
        return artist_DAO.updateArtist(artist)
def editCollection(id=None):
    while id == None or id == '':
        id = input('Enter Collection Id: ')
    collec_DAO = CollectionDAO()
    collec = collec_DAO.getCollectionDetails(id)
    if collec == None:
        return False
    new_name = input('New Name (Empty If No Change): ')
    if new_name != '':
        collec.name = new_name
    list_song = input('New List Song (Empty If No Change) Enter Id Of Song: ')
    if list_song != '':
        collec.listSong = list(map(int, list_song.rstrip().split()))
    cls()
    collec.display()
    confirm = input('Apply Change (Y/N): ')
    if confirm == 'y' or confirm == 'Y':
        return collec_DAO.updateCollection(collec)


def deleteData():
    options = ["\t1. Delete Songs\t\t", "\t2. Delete Artists\t", "\t3. Delete Collections\t", "\t0. Back\t\t\t" ]
    choose = -1
    while(True):   
        print("===========DELETE DATA============")
        for option in options:
            print(chr(19),option,chr(19))
        print("==================================")

        choose = int(input("Choose Your Option: "))
        cls()
        if choose == 1:
            if deleteSong():
                print('Delete Success!')
            else:
                print('Delete Fail!')
        elif choose == 2:
            if deleteArtist():
                print('Delete Success!')
            else:
                print('Delete Fail!')
        elif choose == 3:
            if deleteCollection():
                print('Delete Success!')
            else:
                print('Delete Fail!')
        elif choose == 0:
            break
        else:
            print('Option not found (⊙_⊙;)')
            print('Please try again!')
def deleteSong(id=None):
    while id == None or id == '':
        id = input('Enter Song Id: ')
    song_DAO = SongDAO()
    song = song_DAO.getSongDetails(id)
    if song == None:
        return False
    cls()
    song.display()
    confirm = input('Are You Sure Delete It? (Y/N): ')
    if confirm == 'y' or confirm == 'Y':
        return song_DAO.deleteSong(song.sid, song.name+'.mp3')
def deleteArtist(id=None):
    while id == None or id == '':
        id = input('Enter Artist Id: ')
    artist_DAO = ArtistDAO()
    artist = artist_DAO.getArtistDetails(id)
    if artist == None:
        return False
    cls()
    artist.display()
    confirm = input('Are You Sure Delete It? (Y/N): ')
    if confirm == 'y' or confirm == 'Y':
        return artist_DAO.deleteArtist(artist.aid, artist.name+'.png')
def deleteCollection(id=None):
    while id == None or id == '':
        id = input('Enter Collection Id: ')
    collec_DAO = CollectionDAO()
    collec = collec_DAO.getCollectionDetails(id)
    if collec == None:
        return False
    cls()
    collec.display()
    confirm = input('Are You Sure Delete It? (Y/N): ')
    if confirm == 'y' or confirm == 'Y':
        return collec_DAO.deleteCollection(collec.cid)


if __name__ == "__main__":
    main()