import RPi.GPIO as GPIO
import cv2
import cv2.cv as cv
import numpy as np
import os
import sys, time
from time import sleep
from threading import Thread
GPIO.setwarnings(False)
import pyrebase
import datetime
import time
# The script as below using BCM GPIO 00..nn numbers
GPIO.setmode(GPIO.BCM)

# Set relay pins as output
GPIO.setup(2, GPIO.IN)
count_mag =0;
GPIO.setup(18, GPIO.OUT)
GPIO.output(18, GPIO.HIGH)
cv2.setNumThreads(6)
GPIO.setup(2, GPIO.OUT)
im=0
output_path = "save_wrong_images/"

# firebase config
config = {
    'apiKey': "AIzaSyD7kSsChMjustT4li8a7QYXYZoCCcKgtSs",
    'authDomain': "smart-door-caafe.firebaseapp.com",
    'databaseURL': "https://smart-door-caafe.firebaseio.com",
    'projectId': "smart-door-caafe",
    'storageBucket': "smart-door-caafe.appspot.com",
    'messagingSenderId': "673449216942"
    }
firebase = pyrebase.initialize_app(config)


def get_images(path, size):
    
    sub= 0
    images, labels= [], []
    people= []

    for subdir in os.listdir(path):
        for image in os.listdir(path+ "/"+ subdir):
            #print(subdir, images)
            img= cv2.imread(path+os.path.sep+subdir+os.path.sep+image, cv2.IMREAD_GRAYSCALE)
            img= cv2.resize(img, size)

            images.append(np.asarray(img, dtype= np.uint8))
            labels.append(sub)

            #cv2.imshow("win", img)
            #cv2.waitKey(10)

        people.append(subdir)
        sub+= 1

    return [images, labels, people]

def detect_faces(image):
   
    frontal_face= cv2.CascadeClassifier("haarcascade_frontalface_default.xml")
    bBoxes= frontal_face.detectMultiScale(image, scaleFactor=1.3, minNeighbors=4, minSize=(30, 30), flags = cv.CV_HAAR_SCALE_IMAGE)

    return bBoxes

def train_model(path):
    
    [images, labels, people]= get_images(sys.argv[1], (256, 256))
    #print([images, labels])

    labels= np.asarray(labels, dtype= np.int32)

    # initializing eigen_model and training
    print("Initializing eigen FaceRecognizer and training...")
    sttime= time.clock()
    eigen_model= cv2.createEigenFaceRecognizer()
    eigen_model.train(images, labels)
    print("\tSuccessfully completed training in "+ str(time.clock()- sttime)+ " Secs!")

    return [eigen_model, people]

def majority(mylist):
   
    myset= set(mylist)
    ans= mylist[0]
    ans_f= mylist.count(ans)

    for i in myset:
        if mylist.count(i)> ans_f:
            ans= i
            ans_f= mylist.count(i)

    return ans

def buzzer():
    for i in range(10) :
        GPIO.output(2,GPIO.HIGH)
        sleep(1)
        GPIO.output(2,GPIO.LOW)
        sleep(1)


  

def timestamp():
    return  datetime.datetime.fromtimestamp(time.time()).strftime('%Y-%m-%d %H:%M:%S')

def saveim(paht):
    storage = firebase.storage()
    storage.child(paht).put(paht+".png")
def savename(name):
    db = firebase.database()
    data = {"name": name ,"timestamp":timestamp(),"from":"raspberry"}
    db.child("log-entrace").push(data)



if __name__== "__main__":
    if len(sys.argv)!= 2:
        print("Wrong number of arguments! See the usage.\n")
        print("Usage: face_detrec_video.py <full/path/to/root/images/folder>")
        sys.exit()

    arg_one= sys.argv[1]
    eigen_model, people= train_model(arg_one)
    
    #starts recording video from camera and detects & predict subjects
    cap= cv2.VideoCapture(0)
    cap.set(cv.CV_CAP_PROP_FRAME_WIDTH,160)
    cap.set(cv.CV_CAP_PROP_FRAME_HEIGHT,120)
    c_true= 0
    c_wrong=0;
    last_20= [0 for i in range(20)]
    final_5= []
    box_text= "Subject: "
   
    while(True):
        ret, frame= cap.read()
        gray_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        gray_frame = cv2.equalizeHist(gray_frame)

        bBoxes= detect_faces(gray_frame)

        for bBox in bBoxes:
            (p,q,r,s)= bBox
            cv2.rectangle(frame, (p,q), (p+r,q+s), (225,0,25), 2)

            crop_gray_frame= gray_frame[q:q+s, p:p+r]
            crop_gray_frame= cv2.resize(crop_gray_frame, (256, 256))

            [predicted_label, predicted_conf]= eigen_model.predict(np.asarray(crop_gray_frame))
            last_20.append(predicted_label)
            last_20= last_20[1:]

            box_text= format("Not found!!")


            ch = predicted_conf
            print ch
            print "true: {}".format(c_true)
            print "wrong : {}".format(c_wrong)
            if ch < float(15000):
                c_true+=1
                max_label= majority(last_20)
                #box_text= format("Subject: "+ people[max_label])
                box_text= format("Subject: "+ people[predicted_label])
                
                
                if(c_true%5==0):
                    GPIO.output(18, GPIO.LOW)
                    sleep(3)
                    print "open"
                    c_wrong=0
                    t_name = Thread(target = savename,args=(people[predicted_label],))
                    t_name.setDaemon(True)
                    print people[predicted_label]
                    if not t_name.is_alive() :

                        t_name.start()
                    time.sleep(60) 
                
                
                
            else :
                c_wrong+=1
                box_text= format("Not found!!")
                if(c_wrong%10==0):
                    pahtname = output_path+timestamp()
                    cv2.imwrite(pahtname+".png", frame)  
                    t_buz = Thread(target = buzzer)
                    t_buz.setDaemon(True)
                    t_saveim = Thread(target = saveim,args=(pahtname,))
                    t_saveim.setDaemon(True)
                    if not t_buz.is_alive() or t_saveim.is_alive():
                        t_buz.start()
                        t_saveim.start()
                        print "buzzer start"
                    
            cv2.putText(frame, box_text, (p-20, q-5), cv2.FONT_HERSHEY_PLAIN, 1.3, (25,0,225), 2)                
                  



        cv2.imshow("Video Window", frame)
        if GPIO.input(2):
            count_mag+=1                       
            if(count_mag==10):
                #print "switch is open"
                count_mag=0
        else:
            #print "switch is closed"
            count_mag=0
        #print count_mag
        if (cv2.waitKey(5) & 0xFF== 27):
            break

    cv2.destroyAllWindows()
