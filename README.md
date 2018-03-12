##**Smart Door With Opencv**

  ในปัจจุบัน เมื่อเราออกจากบ้าน หรือไปเที่ยวต่างจังหวัด เราต้องปล่อยทรัพย์สมบัติ ราคาเเพง อาทิ ทอง เครื่องเพชร ใว้ในบ้านโดยไม่มีไครดูเเล จะหวังพึ่งเพื่อนบ้านไห้คอยเป็นหูเป็นตา ก็คงจะดูไห้ไม่ตลอด 24 ชม. อีกทั้ง ก็กลัวโจรจะเข้าบ้านมาโขมยทรัพย์สมบัติเรา จากประสบการณ์ส่วนตัว หากเราติดสัญญาณกันโขมย อยู่ดีๆ มันก็ดังโดยไม่ทราบสาเหตุอีก ทั้งๆ ที่เเค่เเมววิ่งผ่าน  ก็กลัวเพื่อนบ้านจะรำคาณ ดังนั้นจึงเป็นปัญหากวนใจตลอดมา หากจะไปไหนที่ไกลๆ ก็ต้องมาคอยเป็นกังวลเสมอ       ดังนั้น พวกเราจึงได้คิดที่จะทำโครงงานนี้ขึ้นมา เพื่อแก้ปัญหา โจรเข้าบ้านในรูปแบบของอุปกรณ์ ด้วยการการติดกล้องซึ่งเชื่อมต่อกับ raspberry pi ไห้ ระบุบุคคลตรงทางเข้า ก่อนทำการเข้าประตู   ใช้งานพร้อมกับแอปพลิเคชั่นที่ สามารถช่วยเปิด ปิดประตู ผ่านเเอพพลิเคชั่นได้

System architecture
-------------------


[![28685740_1693165384055216_2906260058302054400_o.png](https://s14.postimg.org/rz0msnl1d/28685740_1693165384055216_2906260058302054400_o.png)](https://postimg.org/image/kj1d6uxbx/)


Application features
--------------------
----------

    Github : https://github.com/weerazero/smartdoor_opencv/tree/smartdoot_opencv_android

 - ฟังก์ชั่น แจ้งเตือนเมื่อ เจอบุคคลที่อยู่นอกฐานข้อมูล(**Now Bug 13-03-18**)
 - ฟังก์ชั่นเปิดประตูเมื่อกดปุ่ม
	 - เมื่อคลิ๊กที่ปุ่ม unlock หรือ unlock solenoid door หรือ กลอนประตูจะทำงาน
	 - เมื่อคลิ๊กที่ปุ่ม unlock จะเซฟข้อมูลไปที่ firebase อัตโนมัติ 
	 	

![Alt Text](https://media.giphy.com/media/1fkD6CAgAwJaJS6Ku6/giphy.gif)

 
 - ฟังก์ชั่น เช็ค Log เข้า ออก
 
 - ฟังก์ชั่น แสดงรูปบุคคลต้องสงสัย ที่ไม่ตรงกับ ฐานข้อมูล
 
[![Capture2.png](https://s14.postimg.org/dxxtysm0x/Capture2.png)](https://postimg.org/image/o808y1bwd/)     
 - Authentication ระบุตัวตนเมื่อเข้าระบบ
	 - สามารถดูข้อมูลส่วนตัว เเละ UID ได้ 
   
[![Capturesetting.png](https://s14.postimg.org/pa5u2hadd/Capturesetting.png)](https://postimg.org/image/8z5q65xvh/)
    

System features
--------------------
----------

    Github : https://github.com/weerazero/smartdoor_opencv

- สามารถเปิดประตูได้เมื่อ เจอบุคคลที่อยู่ในฐานข้อมูล
	- เก็บ Log ขึ้น Cloud
- เมื่อเจอบุคคลที่อยู่นอกฐานข้อมูล เสียง Buzzle จะดัง 
- สามารถส่งข้อมูลผ่านเครือข่ายแบบไร้สายไปสู่ระบบ Cloud ได้
- แจ้งเตือนเมื่อประตูเปิดค้างไว้
- จับภาพเมื่อเจอบุคคลที่ไม่ได้อยู่ในฐานข้อมูล เเละ ส่งขึ้น Cloud storage
- Flowchart การทำงนาของระบบ คร่าวๆ

[![flowchart.png](https://s14.postimg.org/8dgra65f5/flowchart.png)](https://postimg.org/image/j0akfldkd/)


System installation on raspberry
--------------------
----------
- Software requirements
	- Download the code from GitHub repository $ git clonehttps://github.com/weerazero/smartdoor_opencv.git
	- Install Pyrebase Available for download from https://github.com/thisbejim/Pyrebase
	- Install OpenCV
- Version 2.4.13.4 is available for download from: https://github.com/opencv/opencv
- Guidelines for Raspberry Pi installation: https://www.pyimagesearch.com/2016/04/18/install-guide-raspberry-pi-3-raspbian-jessie-opencv-3/

- Hardware requirements
	- Raspberry pi
	- Webcam
	- Buzzle 3.3v
	- Magnetic door
	- Solenoid door
	- Relay 3.3v-5v
- Starting a program
	- สร้าง folder ที่ชื่อว่า sorted_output_images 
	- สร้าง folder ใน sorted_output_images เเละ ตั้งชื่อ เป็นชื่อเรา ข้างในจะเป็น รูปที่จะ detect โดยรูป จะต้องแปลงเป็น gray scale
	-  $ ~ cd FaceRecognizer
	-  $ ~ sudo python stream2.py  /sorted_output_images
