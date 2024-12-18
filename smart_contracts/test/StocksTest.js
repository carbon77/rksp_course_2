const { ethers } = require("hardhat")
const { expect } = require("chai")

describe("Stocks", function () {
  let owner
  let sender
  let stocks

  async function sendTx(sender) {
    const value = 100
    const tx = await stocks.connect(sender).pay("APPL", 10, { value })
    await tx.wait()
    return [tx, value]
  }

  beforeEach(async function () {
    [owner, sender] = await ethers.getSigners()
    const Stocks = await ethers.getContractFactory("Stocks", owner)

    stocks = await Stocks.deploy()
    await stocks.getDeployedCode()
  })

  describe("#constructor", async function () {
    it("should be deployed", async function () {
      expect(await stocks.getAddress()).to.be.properAddress
    })

    it("should have 0 ether by default", async function () {
      const balance = await stocks.currentBalance()
      expect(balance).to.eq(0)
    })

    it("should have owner", async function () {
      expect(await stocks.owner()).to.eq(owner.address)
    })
  })

  describe("#pay", async function () {
    it("should be possible to send funds", async function () {
      const [tx, amount] = await sendTx(sender)

      await expect(() => tx)
        .to.changeEtherBalances(
          [stocks, sender], [amount, -amount]
        )

      const newStock = await stocks.getStock(sender.address, 0)

      expect(newStock.code).to.eq("APPL")
      expect(newStock.quantity).to.eq(10)
      expect(newStock.amount).to.eq(amount)
    })

    it("should emit Paid event", async function () {
      const [tx, amount] = await sendTx(sender)

      const timestamp = (
        await ethers.provider.getBlock(tx.blockNumber)
      ).timestamp
      await expect(tx).to.emit(stocks, "Paid")
        .withArgs(sender.address, amount, timestamp)
    })
  })


  describe("#withdraw", async function () {
    it("should allowed owner to withdraw", async function () {
      const [_, amount] = await sendTx(sender)

      const tx = await stocks.withdraw(owner.address)
      await expect(() => tx).to.changeEtherBalances([stocks, owner], [-amount, amount])
    })

    it("should be error when non-owner withdraw", async function () {
      await expect(stocks.connect(sender).withdraw(sender.address)).to.be.revertedWith("you are not an owner!")
    })
  })
})

