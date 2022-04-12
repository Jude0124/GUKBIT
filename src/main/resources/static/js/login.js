function logFormCheck(){
    var id = document.getElementById("id");
    if(id.value==""){
        alert("아이디를 입력하세요.");
        id.focus(); 
        return false;
    }
    var pwd = document.getElementById("password");
    if(pwd.value==""){
        alert("비밀번호를 입력하세요.");
        pwd.focus(); 
        return false;
    }
    return true;
}