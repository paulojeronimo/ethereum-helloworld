fs = require('fs');
Web3 = require('web3');
solc = require('solc');

web3 = new Web3(new Web3.providers.HttpProvider('http://localhost:8545'));

if (!web3.isConnected()) {
  console.log('web3 fail to connect!');
  return;
}

console.log('web3.version.api: ' + web3.version.api);
console.log('web3.version.ethereum: ' + web3.version.ethereum);
console.log('web3.version.network: ' + web3.version.network);
console.log('web3.version.node: ' + web3.version.node);

compiledCode = solc.compile(fs.readFileSync('contracts/HelloWorld.sol').toString());
helloWorldContract = web3.eth.contract(JSON.parse(compiledCode.contracts[':HelloWorld'].interface));

var message = 'Hello Ethereum world!';

if (process.argv.length == 3) {
  if (process.argv[2].startsWith('0x')) {
    var address = process.argv[2];
    console.log('address: ' + address);
    var helloWorld = helloWorldContract.at(address);
    console.log('getMessage(): ' + helloWorld.getMessage());
    return;
  } else {
    message = process.argv[2];
  }
}

helloWorldContract.new({
    from: web3.eth.accounts[0],
    data: '0x' + compiledCode.contracts[':HelloWorld'].bytecode,
    gas: 4700000
  },
  function (err, contract) {
    if (err) {
      console.log(err);
      return;
    }
    if (contract.address) {
      console.log('contract transaction mined!' +
        '\naddress: ' + contract.address);
      console.log('getMessage(): ' + contract.getMessage());

      var tx = contract.setMessage(message, {from:web3.eth.accounts[0]});
      console.log('setMessage("' + message + '") transaction: ' + tx);
    } else {
      console.log('contract transaction created!' +
        '\ntransactionHash: ' + contract.transactionHash);
    }
  }
);
