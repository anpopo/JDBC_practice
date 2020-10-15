    ---------------- ******** SYS로 접속쓰~!~~~@@~!!~!
    
    show user;
    --USER이(가) "SYS"입니다.
    
    -- 오라클 일반 사용자 계정을 생성해 준다. -- 
    
    create user myorauser identified by cclass;
    
    -- 생성된 myorauser 에게 오라클 서버에 접속해서 작업을 할 수 있도록 권한을 부여해 준다....! --
    grant connect, resource to myorauser;
    ------------------------------------------------------------------------------------------------------------------------------------ 
    -- ******** myorauser로 접속쓰~!~~~@@~!!~!
    show user;
    -- USER이(가) "MYORAUSER"입니다.
    
    select * from tab;
    
            ------ ***  회원 테이블 생성하기  *** ------
    drop table jdbc_member purge; 
            
    create table jdbcmember
    (userseq       number        not null    -- 회원번호
    ,userid        varchar2(30)  not null    -- 회원아이디
    ,passwd        varchar2(30)  not null    -- 회원암호
    ,name          varchar2(20)  not null    -- 회원명
    ,mobile        varchar2(20)              -- 연락처
    ,point         number(10) default 0      -- 포인트
    ,registerday   date default sysdate      -- 가입일자 
    ,status        number(1) default 1       -- status 컬럼의 값이 1 이면 정상, 0 이면 탈퇴 
    ,constraint PK_jdbcmember primary key(userseq)
    ,constraint UQ_jdbcmember unique(userid)
    ,constraint CK_jdbcmember check( status in(0,1) )
    );
    
    create sequence userseq
    start with 1
    increment by 1
    nomaxvalue
    nominvalue
    nocycle
    nocache;
    
    select *
    from jdbc_member
    order by userseq desc;
    
    select *
    from user_tables;
    
    delete from jdbc_member where userid = '1';
