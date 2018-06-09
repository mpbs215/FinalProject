drop table notice;
drop table terms;
drop table faq;
drop table review;
drop table parkImage;
drop table carType;
drop table authority;
drop table seller;
drop table qna;
drop table parkReserve;
drop table parkRegist;
drop table Park;
drop table UserInfo;

select reserveno,parkname,reservestart,reserveend,r.userid,price*(reserveend-reservestart)*24 price from park p join parkreserve r using(parkno) 
where p.userid=0 and reserveend<sysdate

create table UserInfo
(
		userId			varchar2(100)	primary key,
		password	varchar2(100) NOT NULL,
		userName   varchar2(100) NOT NULL,
		email			varchar2(100) NOT NULL,
		hp				varchar2(100) NOT NULL,
		address		varchar2(100) NOT NULL,
		regidate		timestamp NOT NULL,
		seller			char(1)
);

insert into userinfo values('KimD','1234','�起Ȳ','mpbs215@naver.com','010-9324-9482','���� ���� �ұ���',sysdate,0);
insert into userinfo values('KimM','1234','��μ�','KimM@daum.com','010-9324-9482','�뱸 ������',sysdate,0);
insert into userinfo values('LeeJ','2345','���繮','LeeJaeDoor@naver.com','010-9324-9482','û��',sysdate,1);
insert into userinfo values('JeeM','1234','������','moonkyong@naver.com','010-9324-9482','����',sysdate,0);
insert into userinfo values('KonE','3456','������','silverRain@naver.com','010-9324-9482','����',sysdate,0);

create table Park
(
		parkNo				number 		   primary key,
		parkName			varchar2(100)  NOT NULL,
		userId   			varchar2(100)  NOT NULL constraint park_userId_fk references userinfo(userId),
		parkAddr			varchar2(200)  NOT NULL,
		parkSize			long 		   NOT NULL,
		parkContent 		varchar2(2000) NOT NULL,
		price				number 		   NOT NULL,
		maxCunsumer 		number		   NOT NULL,
		latitude			varchar2(100)  NOT NULL,
		longtitude			varchar2(100)  NOT NULL
);

insert into park values
(park_seq.nextval,'�繮������1ȣ','LeeJ','������ �д籸 ���ڵ� 178-1',
'500.5','���ִ�������',2000,5,'37.3594835','127.10520759999997')

insert into park values
(park_seq.nextval,'�繮������2ȣ','LeeJ','���� �ұ��� 243-13���� 201',
'50','�ν���������',500,3,'37.6143768','126.93204160000005')

insert into park values
(park_seq.nextval,'�繮������3ȣ','LeeJ','����Ư���� ���α� �ۿ��� 48 ����Ư���ñ���û',
'5000','����Ư���ñ���û������',50000,10,'37.5701647','126.96724840000002')



create table parkRegist
(
		parkNo			number	  primary key constraint regist_parkno_fk references park(parkNo),
		regiStart		Timestamp NOT NULL,
		regiEnd			Timestamp NOT NULL
);

create table parkReserve
(
		reserveNo		number 			primary key,
		userId			varchar2(100)	NOT NULL constraint reserve_userId_fk references userinfo(userId),
		parkNo 			number 			NOT NULL constraint reserve_parkno_fk 	references parkRegist(parkNo),
		reserveStart	Timestamp		NOT NULL,
		reserveEnd		Timestamp  		NOT NULL
);

create table review
(
		userId			varchar2(100)	constraint	review_userId_fk references userinfo(userId),
		parkNo			number		 	constraint	review_parkno_fk references parkRegist(parkno),
		rating			number			NOT NULL,			
		reviewContent	varchar2(500)	NOT NULL,
		
		CONSTRAINT review_pk PRIMARY KEY (userId, parkNo)
);

create table parkImage
(
		imgNo		number 		  primary key,
		parkNo		number		  NOT NULL constraint	img_parkno_fk	references parkRegist(parkNo),
		imgPath		varchar2(100) NOT NULL
);

create table carType
(		
		parkNo	number			constraint	carType_parkno_fk	references parkRegist(parkNo),
		carType varchar2(100) ,
		CONSTRAINT carType_pk PRIMARY KEY (parkNo,carType)
);

create table seller
(
		userId		varchar2(100) primary key constraint	seller_userId_fk references userinfo(userId),
		account		varchar2(200) NOT NULL,
		realname	varchar2(200) NOT NULL
);

create table authority
(
		userId	varchar2(100) constraint authority_userId_fk references userinfo(userId),
		role	varchar2(100),
		CONSTRAINT authority_pk PRIMARY KEY (userId,role)
);

create table qna
(
		qnaNo		number primary key,
		userId		varchar2(100) constraint qna_userId_fk references userinfo(userId),
		QNASubject  varchar2(200) NOT NULL,
		QNAContent  varchar2(2000) NOT NULL,
		QNARegidate TIMESTAMP NOT NULL,
		QNAReview	varchar2(2000),
		QNAReviewRegidate timestamp,
		QNAHIT		number NOT NULL,
		QNAPWD		number
);

create table faq(
		faqNo	number	primary key,
		faqSubject varchar2(200) not null,
		faqContent varchar2(2000) not null
);

create table terms(
		termsno	number primary key,
		termsSubject varchar2(200) not null,
		termsContent varchar2(2000) not null
);


create table notice(
		noticeNo	number primary key,
		noticesubject varchar2(200) NOT NULL,
		noticeRegidate timestamp NOT NULL,
		noticeContent varchar2(2000) NOT NULL,
		noticeHit	  number NOT NULL,
		noticeImage	  varchar2(100)
);



commit