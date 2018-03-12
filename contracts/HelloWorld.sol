pragma solidity ^0.4.0;

contract HelloWorld {
    string message;
    
    function HelloWorld() public {
        setMessage("Hello world!");
    }
    
    function setMessage(string _message) public {
        message = _message;
    }
    
    function getMessage() public constant returns (string) {
        return message;
    }
}
