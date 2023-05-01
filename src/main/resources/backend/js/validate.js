
function isValidUsername (str) {
  return ['admin', 'editor'].indexOf(str.trim()) >= 0;
}

function isExternal (path) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

function isCellPhone (val) {
  if (!/^1(3|4|5|6|7|8)\d{9}$/.test(val)) {
    return false
  } else {
    return true
  }
}

//check account
function checkUserName (rule, value, callback){
  if (value == "") {
    callback(new Error("account"))
  } else if (value.length > 20 || value.length <3) {
    callback(new Error("length should 3-20"))
  } else {
    callback()
  }
}

//check name
function checkName (rule, value, callback){
  if (value == "") {
    callback(new Error("Name"))
  } else if (value.length > 12) {
    callback(new Error("Length of the account"))
  } else {
    callback()
  }
}

function checkPhone (rule, value, callback){
   //uk phone number
    let reg = /^(\+?44|0)7\d{9}$/;
  if (value == "") {
    callback(new Error("enter your phone number"))
  } else if (reg.test(value) == false) {
    callback(new Error("phone number is not valid"))
  } else {
    callback()
  }
}


function validID (rule,value,callback) {
  //email
    let reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
  if(value == '') {
    callback(new Error('enter email'))
  } else if (reg.test(value)) {
    callback()
  } else {
    callback(new Error('email is not valid'))
  }
}