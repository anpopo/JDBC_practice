    show user;  -- USER이(가) "MYORAUSER"입니다.
    
    select *
    from tab;
    
    select *
    from seq;
    
    select *
    from user_constraints;
    
    
    ------ ***  회원 테이블 생성하기  *** ------
    drop table jdbc_member purge;
            
    create table jdbc_member
    (userseq       number        not null    -- 회원번호
    ,userid        varchar2(30)  not null    -- 회원아이디
    ,passwd        varchar2(30)  not null    -- 회원암호
    ,name          varchar2(20)  not null    -- 회원명
    ,mobile        varchar2(20)              -- 연락처
    ,point         number(10) default 0      -- 포인트
    ,registerday   date default sysdate      -- 가입일자 
    ,status        number(1) default 1       -- status 컬럼의 값이 1 이면 정상, 0 이면 탈퇴 
    , constraint PK_jdbc_member primary key(userseq)
    , constraint UQ_jdbc_member unique(userid)
    , constraint CK_jdbc_member check(status in(0,1))
    , constraint CK_jdbc_member_point check(point < 30)
    );
    
    drop sequence userseq;
    
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
    
    
    
    ----------------------------------------------------------------------------------------------
    drop table jdbc_board purge;
    
                ------ ///  게시판 테이블 생성하기  /// ------
    create table jdbc_board
    (boardno       number        not null          -- 글번호
    ,fk_userid     varchar2(30)  not null          -- 작성자아이디
    ,subject       varchar2(100) not null          -- 글제목
    ,contents      varchar2(200) not null          -- 글내용
    ,writeday      date default sysdate not null   -- 작성일자
    ,viewcount     number default 0 not null       -- 조회수 
    ,boardpasswd   varchar2(20) not null           -- 글암호 
    ,constraint PK_jdbc_board primary key(boardno)
    ,constraint FK_jdbc_board foreign key(fk_userid) references jdbc_member(userid) 
    );
    
    drop sequence board_seq ;
    
    create sequence board_seq
    start with 1
    increment by 1
    nomaxvalue
    nominvalue
    nocycle
    nocache;
    
    ------------------------------------------------------------------------------------------------------------------------------
    drop table jdbc_comment purge;
    
            ------ ///  댓글 테이블 생성하기  /// ------
    create table jdbc_comment 
    (commentno   number        not null    -- 댓글번호 
    ,fk_boardno  number        not null    -- 원글의 글번호 
    ,fk_userid   varchar2(30)  not null    -- 사용자ID
    ,contents    varchar2(200) not null    -- 댓글내용 
    ,writeday    date default sysdate      -- 작성일자
    ,constraint  PK_jdbc_comment  primary key(commentno) 
    ,constraint  FK_jdbc_comment_fk_boardno foreign key(fk_boardno) 
                 references jdbc_board(boardno) on delete cascade 
    ,constraint  FK_jdbc_comment_fk_userid  foreign key(fk_userid) 
                 references jdbc_member(userid) 
    );
    
    drop sequence seq_comment;
    
    create sequence seq_comment
    start with 1
    increment by 1
    nomaxvalue
    nominvalue
    nocycle
    nocache;
    
    --------------------------------------------------------------------------
    
    rollback;
    
    select *
    from jdbc_member
    order by userseq desc;
    
    
    insert into jdbc_member(userseq, userid, passwd, name, mobile)
    values(userseq.nextval, ?, ?, ?, ?);
    
    select userseq
    , userid
    , passwd
    , name
    , mobile
    , point
    , to_char(registerday, 'yyyy-mm-dd') as registerday
    , status
    from jdbc_member;
    where userid = ? and passwd = ?;
    
    
    insert into jdbc_board(BOARDNO, fk_userid, subject, contents, boardpasswd)
    values(board_seq, ?, ?, ?, ?);
    
    update jdbc_member set point = point + 10;
    where userid = ?;
    
    
    
    select b.boardno
    , b.subject
    , m.name
    , to_char(b.writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday
    , b.viewcount
    , c.commentcnt
    from jdbc_board b join jdbc_member m
    on b.fk_userid = m.userid
    left join (
        select fk_boardno
        , count(*) as commentcnt
        from jdbc_comment
        group by fk_boardno
    ) c
    on b.boardno = c.fk_boardno
    order by 1 desc;
    
    select contents
    , fk_userid
    , subject
    from jdbc_board;
    
    update jdbc_board set viewcount = viewcount + 1;
    where boardno = ?;
    
    
    select c.contents
    , m.name
    , to_char(c.writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday
    from jdbc_comment c join jdbc_member m
    on c.fk_userid = m.userid
    where c.fk_boardno = ?
    order by c.commentno desc;
    
    insert into jdbc_comment(commentno, fk_boardno, fk_userid, contents)
    values(seq_comment.nextval, ?, ?, ?);
    
    delete from jdbc_board;
    where boardno = ? and boardpasswd = ?;
    
    
    /*
        [문법]
        create or replace function 함수명
        (파라미터변수명 in 파라미터변수명의 타입)
        return 리턴시킬타입
        is 
            변수선언;
        begin
            명령어;
            return 값;
        end 함수명;
    */
    
    -- 특정날짜를 입력받아서 그 날짜의 자정의 값을 반환시켜주는 함수를 생성해보자
    create or replace function func_midnight
    (p_date in date)
    return date
    is
    begin
        return to_date(to_char(p_date, 'yyyy-mm-dd'), 'yyyy-mm-dd');
    end func_midnight;
    
    
    select count(*) as total
    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 6, 1, 0)) as prev6
    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 5, 1, 0)) as prev6
    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 4, 1, 0)) as prev6
    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 3, 1, 0)) as prev6
    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 2, 1, 0)) as prev6
    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 1, 1, 0)) as prev6
    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 0, 1, 0)) as prev6
    from jdbc_board
    where (func_midnight(sysdate) - func_midnight(writeday)) < 7;
    
    
    select case grouping(to_char(writeday, 'yyyy-mm-dd')) when 0 then to_char(writeday, 'yyyy-mm-dd') else '전체' end as writeday
    , count(*) as writecount
    from jdbc_board;
    where to_char(writeday, 'yyyy-mm') = to_char(sysdate, 'yyyy-mm')
    group by rollup(to_char(writeday, 'yyyy-mm-dd'));
    
    
    select userseq
    , m.userid
    , name
    , mobile
    , point
    , registerday
    , case status when 1 then '회원' else '탈퇴' end as status
    , b.writecount
    , c.commentcount
    from jdbc_member m join 
    (select fk_userid as userid
    , count(*) as writecount
    from jdbc_board
    group by fk_userid
    ) b
    on m.userid = b.userid
    join 
    (select fk_userid as userid
    , count(*) as commentcount
    from jdbc_comment
    group by fk_userid
    ) c
    on m.userid = c.userid;
    
    select *
    from jdbc_board;
    
    select *
    from jdbc_comment;