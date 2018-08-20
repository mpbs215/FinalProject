drop table notice_TB;
drop table terms_TB;
drop table faq_TB;
drop table review_TB;
drop table park_Image_TB;
drop table car_Type_TB;
drop table authority_TB;
drop table seller_TB;
drop table qna_TB;
drop table park_Reserve_TB;
drop table park_Regist_TB;
drop table Park_TB;
drop table sms_tb;
drop table User_Info_TB;

create table USER_INFO_TB
(
      USER_ID         varchar2(100)   primary key,
      password   varchar2(100) NOT NULL,
      USER_NAME   varchar2(100) NOT NULL,
      email         varchar2(100) NOT NULL,
      hp            varchar2(100) NOT NULL,
      address      varchar2(100) NOT NULL,
      regidate      timestamp NOT NULL,
      seller         char(1)
);

create table PARK_TB
(
      park_No            number          primary key,
      user_Id            varchar2(100)  NOT NULL constraint park_userId_fk references user_info_tb(user_Id) ON DELETE CASCADE,
      park_Name         varchar2(100)  NOT NULL,
      park_Addr         varchar2(200)  NOT NULL,
      park_Size         varchar2(100)   NOT NULL,
      park_Content       varchar2(2000) NOT NULL,
      price            number          NOT NULL
       
);

create table park_Regist_TB
(
      park_No         number     primary key constraint regist_parkno_fk references park_tb(park_No) ON DELETE CASCADE,
      regi_Start      Timestamp NOT NULL,
      regi_End         Timestamp NOT NULL
);

create table park_Reserve_TB
(
      reserve_No      number          primary key,
      park_No          number          NOT NULL constraint reserve_parkno_fk    references park_Regist_tb(park_No) ON DELETE CASCADE,
      user_Id         varchar2(100)   NOT NULL constraint reserve_userId_fk references user_info_tb(user_Id) ON DELETE CASCADE,
      reserve_Start   Timestamp      NOT NULL,
      reserve_End      Timestamp        NOT NULL,
      car_Type         varchar2(100)   NOT NULL
);

create table review_TB
(
      user_Id         varchar2(100)   constraint   review_userId_fk references user_info_tb(user_Id) ON DELETE CASCADE,
      park_No         number          constraint   review_parkno_fk references park_Regist_tb(park_no) ON DELETE CASCADE,
      rating         number         NOT NULL,         
      review_Content   varchar2(500)   NOT NULL,
      
      CONSTRAINT review_pk PRIMARY KEY (user_Id, park_No)
);

create table park_Image_TB
(
      img_No      number         primary key,
      park_No      number        NOT NULL constraint   img_parkno_fk   references park_Regist_tb(park_No) ON DELETE CASCADE,
      img_Path      varchar2(100) NOT NULL
);


create table car_Type_TB
(      
      park_No   number         constraint   carType_parkno_fk   references park_Regist_tb(park_No) ON DELETE CASCADE,
      car_Type varchar2(100) ,
      max_Car  number not null,
      CONSTRAINT carType_pk PRIMARY KEY (park_No,car_Type)
);

create table seller_TB
(
      user_Id      varchar2(100) primary key constraint   seller_userId_fk references user_info_tb(user_Id) ON DELETE CASCADE,
      account      varchar2(200) NOT NULL,
      real_name   varchar2(200) NOT NULL
);

create table authority_TB
(
      user_Id   varchar2(100) constraint authority_userId_fk references user_info_tb(user_Id) ON DELETE CASCADE,
      role   varchar2(100),
      key varchar2(10),
      hp varchar2(20),
      CONSTRAINT authority_pk PRIMARY KEY (user_Id)
);

create table qna_TB
(
      qna_No      number primary key,
      user_Id      varchar2(100) constraint qna_userId_fk references user_info_tb(user_Id) ON DELETE CASCADE,
      QNA_Sub  varchar2(200) NOT NULL,
      QNA_Content  varchar2(2000) NOT NULL,
      QNA_DT TIMESTAMP NOT NULL,
      QNA_Review   varchar2(2000),
      QNA_Review_DT timestamp,
      QNA_HIT      number NOT NULL,
      QNA_PWD      number,
      QNA_IMAGE varchar2(100)
);

create table faq_TB(
      faq_No   number   primary key,
      faq_Sub varchar2(200) not null,
      faq_Content varchar2(2000) not null
);

create table terms_TB(
      terms_no   number primary key,
      terms_Sub varchar2(200) not null,
      terms_Content varchar2(2000) not null
);

create table notice_TB(
      notice_No   number primary key,
      notice_sub varchar2(200) NOT NULL,
      notice_DT timestamp NOT NULL,
      notice_Content varchar2(2000) NOT NULL,
      notice_Hit     number NOT NULL,
      notice_Image     varchar2(100)
);

create table SMS_TB (
        user_id varchar2(100) PRIMARY KEY CONSTRAINT user_id REFERENCES USER_INFO_TB(user_id) ON DELETE CASCADE,
        hp varchar2(20) not null,
        key varchar2(100) not null
);

drop sequence park_seq;
drop sequence img_seq;
drop sequence faq_seq;
drop sequence terms_seq;
drop sequence notice_seq;
drop sequence reserve_seq;
drop sequence qna_seq;

create sequence park_seq 
start with 1
increment BY 1 
maxvalue 10000 
nocache 
nocycle;

create sequence img_seq 
start with 1 
increment BY 1 
maxvalue 10000 
nocache 
nocycle;

create sequence faq_seq 
start with 1 
increment BY 1 
maxvalue 10000 
nocache 
nocycle;

create sequence terms_seq 
start with 1 
increment BY 1 
maxvalue 10000 
nocache 
nocycle;

create sequence notice_seq 
start with 1 
increment BY 1 
maxvalue 10000 
nocache 
nocycle;

create sequence reserve_seq 
start with 1 
increment BY 1 
maxvalue 10000 
nocache 
nocycle;

create sequence qna_seq 
start with 1 
increment BY 1 
maxvalue 10000 
nocache 
nocycle;

create table SIDOGUGUNDONGRI(
    sido varchar2(100),
    dong varchar2(100),
    gugun varchar2(100),
    ri varchar2(100)
);

select *from user_info_tb