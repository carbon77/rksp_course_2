// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

contract Stocks {
  struct Stock {
    string code;
    uint256 amount;
    uint256 quantity;
  }

  struct Balance {
    uint totalStocks;
    Stock[] stocks;    
  }

  modifier onlyOwner() {
    require(msg.sender == owner, "you are not an owner!");
    _;
  }

  event Paid(address _from, uint _amount, uint _timestamp);

  mapping (address => Balance) public balances;
  address public owner;

  constructor() {
    owner = msg.sender;
  }

  function withdraw(address payable _to) external onlyOwner {
    _to.transfer(address(this).balance);
  }

  function currentBalance() public view returns(uint) {
    return address(this).balance;
  }

  function getStock(address _owner, uint idx) public view returns(Stock memory) {
    return balances[_owner].stocks[idx];
  }

  function getStocks(address _owner) public view returns(Stock[] memory) {
    return balances[_owner].stocks;
  }

  function pay(string memory code, uint256 quantity) external payable {
    emit Paid(msg.sender, msg.value, block.timestamp);
    balances[msg.sender].totalStocks++;
    Stock memory stock = Stock(code, msg.value, quantity);
    balances[msg.sender].stocks.push(stock);
  }
}

