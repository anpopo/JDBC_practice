    show user;  -- USER이(가) "HR"입니다.
    
    create table jdbc_tbl_memo
    (no                 number(4)
    , name              varchar2(20) not null
    , msg               varchar2(200) not null
    , writeday          date default sysdate
    , constraint PK_jdbc_tbl_memo_no primary key (no)
    );
    
    create sequence jdbc_seq_memo
    start with 1
    increment by 1
    nomaxvalue
    nominvalue
    nocycle
    nocache;
    
    
    select *
    from jdbc_tbl_memo
    order by 1;
    
    select no
    , name
    , msg
    , to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday
    from jdbc_tbl_memo
    order by no desc;
    
    insert into jdbc_tbl_memo(no, name, msg)
    values (jdbc_seq_memo.nextval, ?, ?);
    
    update jdbc_tbl_memo ;
    set name = '', msg = ''
        where no = '';
        
        
    delete from jdbc_tbl_memo;
    where no = '';
    