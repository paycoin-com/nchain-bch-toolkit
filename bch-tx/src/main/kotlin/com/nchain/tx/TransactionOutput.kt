/*
 * Copyright 2011 Google Inc.
 * Copyright 2014 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nchain.tx

import com.nchain.address.CashAddress
import com.nchain.key.ECKey
import com.nchain.params.NetworkParameters
import com.nchain.script.Script
import com.nchain.script.ScriptBuilder
import com.nchain.script.ScriptException
import com.nchain.shared.ProtocolException
import com.nchain.shared.VarInt
import com.nchain.tools.ByteUtils
import com.nchain.tools.MessageReader
import com.nchain.tools.UnsafeByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

/**
 *
 * A TransactionOutput message contains a scriptPubKey that controls who is able to spend its value. It is a sub-part
 * of the Transaction message.
 *
 * Instances of this class thread safe
 */
class TransactionOutput(val value: Coin = Coin.ZERO,
                        val scriptBytes: ByteArray) {

    // These fields are not Bitcoin serialized. They are used for tracking purposes in our wallet
    // only. If set to true, this output is counted towards our balance. If false and spentBy is null the tx output
    // was owned by us and was sent to somebody else. If false and spentBy is set it means this output was owned by
    // us and used in one of our own transactions (eg, because it is a change output).
    /**
     * Returns whether [TransactionOutput.markAsSpent] has been called on this class. A
     * [Wallet] will mark a transaction output as spent once it sees a transaction input that is connected to it.
     * Note that this flag can be false when an output has in fact been spent according to the rest of the network if
     * the spending transaction wasn't downloaded yet, and it can be marked as spent when in reality the rest of the
     * network believes it to be unspent if the signature or script connecting to it was not actually valid.
     */
//    var isAvailableForSpending: Boolean = false
//        private set
    /**
     * Returns the connected input.
     */
//    var spentBy: TransactionInput? = null
//        private set

    val length: Int

    init {
        length = 8 + VarInt.sizeOf(scriptBytes.size.toLong()) + scriptBytes.size
    }


    /**
     * Creates an output that sends 'value' to the given address (public key hash). The amount should be created with
     * something like [Coin.valueOf]. Typically you would use
     * [Transaction.addOutput] instead of creating a TransactionOutput directly.
     */
    constructor(value: Coin, to: CashAddress) : this(value, ScriptBuilder.createOutputScript(to).listProgram()) {}

    /**
     * Creates an output that sends 'value' to the given public key using a simple CHECKSIG script (no addresses). The
     * amount should be created with something like [Coin.valueOf]. Typically you would use
     * [Transaction.addOutput] instead of creating an output directly.
     */
    constructor(value: Coin, to: ECKey) : this(value, ScriptBuilder.createOutputScript(to).listProgram()) {}


    /**
     * Gets the index of this output in the parent transaction, or throws if this output is free standing. Iterates
     * over the parents list to discover this.
     */
/*
    open val index: Int
        get() {
            val outputs = parentTransaction!!.getOutputs()
            for (i in outputs.indices) {
                if (outputs[i] === this)
                    return i
            }
            throw IllegalStateException("Output linked to wrong parent transaction?")
        }
*/

    /**
     * Will this transaction be relayable and mined by default miners?
     */
    // Transactions that are OP_RETURN can't be dust regardless of their value.
//    val isDust: Boolean
//        get() = if (scriptPubKey.isOpReturn) false else getValue().isLessThan(minNonDustValue)

    /**
     * Returns the minimum value for this output to be considered "not dust", i.e. the transaction will be relayable
     * and mined by default miners. For normal pay to address outputs, this is 2730 satoshis, the same as
     * [Transaction.MIN_NONDUST_OUTPUT].
     */
//    val minNonDustValue: Coin
//        get() = getMinNonDustValue(Transaction.REFERENCE_DEFAULT_MIN_TX_FEE.multiply(3))

    /**
     * Returns the transaction that owns this output.
     */
//    var parentTransaction: Transaction? = null
//        get() = parent as Transaction?

    val isOpReturn: Boolean
        get() = scriptPubKey.isOpReturn == true

    val opReturnData: ByteArray?
        get() {
            return if (isOpReturn) scriptPubKey.chunks.get(1).data else null
        }


    /**
     * Returns the transaction hash that owns this output.
     */
//    open val parentTransactionHash: Sha256Hash?
//        get() = if (parent == null) null else parent!!.hash
//
    /**
     * Returns the depth in blocks of the parent tx.
     *
     *
     * If the transaction appears in the top block, the depth is one. If it's anything else (pending, dead, unknown)
     * then -1.
     * @return The tx depth or -1.
     */
/*
    open val parentTransactionDepthInBlocks: Int
        get() {
            if (parentTransaction != null) {
                val confidence = parentTransaction!!.getConfidence()
                if (confidence.getConfidenceType() == TransactionConfidence.ConfidenceType.BUILDING) {
                    return confidence.depthInBlocks
                }
            }
            return -1
        }
*/

    /**
     * Returns a new [TransactionOutPoint], which is essentially a structure pointing to this output.
     * Requires that this output is not detached.
     */
/*
    val outPointFor: TransactionOutPoint
        get() = TransactionOutPoint(params!!, index.toLong(), parentTransaction)
*/

    /**
     * Deserializes a transaction output message. This is usually part of a transaction message.
     */
/*
    @Throws(ProtocolException::class)
    constructor(params: NetworkParameters, parent: Transaction?, payload: ByteArray,
                offset: Int) : super(params, payload, offset) {
        this.parent =(parent)
        isAvailableForSpending = true
    }
*/

    /**
     * Deserializes a transaction output message. This is usually part of a transaction message.
     *
     * @param params NetworkParameters object.
     * @param payload Bitcoin protocol formatted byte array containing message content.
     * @param offset The location of the first payload byte within the array.
     * @param serializer the serializer to use for this message.
     * @throws ProtocolException
     */
/*
    @Throws(ProtocolException::class)
    constructor(params: NetworkParameters, parent: Transaction?, payload: ByteArray, offset: Int, serializer: MessageSerializer) : super(params, payload, offset, parent, serializer, Message.UNKNOWN_LENGTH) {
        isAvailableForSpending = true
    }
*/

    private var _scriptPubKey: Script? = null

    val scriptPubKey: Script
        @Throws(ScriptException::class)
        get() {
            if (_scriptPubKey == null) {
                _scriptPubKey = Script(scriptBytes)
            }
            return _scriptPubKey!!
        }

    /**
     *
     * If the output script pays to an address as in [
 * P2PKH](https://bitcoinkt.org/en/developer-guide#term-p2pkh), return the address of the receiver, i.e., a base58 encoded hash of the public key in the script.
     *
     * @param networkParameters needed to specify an address
     * @return null, if the output script is not the form *OP_DUP OP_HASH160 <PubkeyHash> OP_EQUALVERIFY OP_CHECKSIG</PubkeyHash>*,
     * i.e., not P2PKH
     * @return an address made out of the public key hash
     */
    @Throws(ScriptException::class)
    fun getAddressFromP2PKHScript(networkParameters: NetworkParameters): CashAddress? {
        return if (scriptPubKey.isSentToAddress) scriptPubKey.getToAddress(networkParameters) else null

    }

    /**
     *
     * If the output script pays to a redeem script, return the address of the redeem script as described by,
     * i.e., a base58 encoding of [one-byte version][20-byte hash][4-byte checksum], where the 20-byte hash refers to
     * the redeem script.
     *
     *
     * P2SH is described by [BIP 16](https://github.com/bitcoinkt/bips/blob/master/bip-0016.mediawiki) and
     * [documented in the Bitcoin Developer Guide](https://bitcoinkt.org/en/developer-guide#p2sh-scripts).
     *
     * @param networkParameters needed to specify an address
     * @return null if the output script does not pay to a script hash
     * @return an address that belongs to the redeem script
     */
    @Throws(ScriptException::class)
    fun getAddressFromP2SH(networkParameters: NetworkParameters): CashAddress? {
        return if (scriptPubKey.isPayToScriptHash) scriptPubKey.getToAddress(networkParameters) else null

    }

    @Throws(IOException::class)
    fun bitcoinSerialize(): ByteArray {
        val stream = UnsafeByteArrayOutputStream()
        bitcoinSerializeToStream(stream)
        stream.close()
        return stream.toByteArray()
    }


    @Throws(IOException::class)
    fun bitcoinSerializeToStream(stream: OutputStream) {
        ByteUtils.int64ToByteStreamLE(value.value, stream)
        // TODO: Move script serialization into the Script class, where it belongs.
        stream.write(VarInt(scriptBytes.size.toLong()).encode())
        stream.write(scriptBytes)
    }


    /**
     *
     * Gets the minimum value for a txout of this size to be considered non-dust by Bitcoin Core
     * (and thus relayed). See: CTxOut::IsDust() in Bitcoin Core. The assumption is that any output that would
     * consume more than a third of its value in fees is not something the Bitcoin system wants to deal with right now,
     * so we call them "dust outputs" and they're made non standard. The choice of one third is somewhat arbitrary and
     * may change in future.
     *
     *
     * You probably should use [org.bitcoinj.core.TransactionOutput.getMinNonDustValue] which uses
     * a safe fee-per-kb by default.
     *
     * @param feePerKb The fee required per kilobyte. Note that this is the same as Bitcoin Core's -minrelaytxfee * 3
     */
/*
    fun getMinNonDustValue(feePerKb: Coin): Coin {
        // A typical output is 33 bytes (pubkey hash + opcodes) and requires an input of 148 bytes to spend so we add
        // that together to find out the total amount of data used to transfer this amount of value. Note that this
        // formula is wrong for anything that's not a pay-to-address output, unfortunately, we must follow Bitcoin Core's
        // wrongness in order to ensure we're considered standard. A better formula would either estimate the
        // size of data needed to satisfy all different script types, or just hard code 33 below.
        val size = (this.unsafeBitcoinSerialize().size + 148).toLong()
        return feePerKb.multiply(size).divide(1000)
        return null
    }
*/

    /**
     * Sets this objects availableForSpending flag to false and the spentBy pointer to the given input.
     * If the input is null, it means this output was signed over to somebody else rather than one of our own keys.
     * @throws IllegalStateException if the transaction was already marked as spent.
     */
/*
    fun markAsSpent(input: TransactionInput) {
        checkState(isAvailableForSpending)
        isAvailableForSpending = false
        spentBy = input
        if (parent != null)
            if (log.isDebugEnabled())
                log.debug("Marked {}:{} as spent by {}", parentTransactionHash, index, input)
            else if (log.isDebugEnabled()) log.debug("Marked floating output as spent by {}", input)
    }
*/

    /**
     * Resets the spent pointer / availableForSpending flag to null.
     */
/*
    fun markAsUnspent() {
        if (parent != null)
            if (log.isDebugEnabled())
                log.debug("Un-marked {}:{} as spent by {}", parentTransactionHash, index, spentBy)
            else if (log.isDebugEnabled()) log.debug("Un-marked floating output as spent by {}", spentBy)
        isAvailableForSpending = true
        spentBy = null
    }
*/

    /**
     * Returns true if this output is to a key in the wallet or to an address/script we are watching.
     */
//    fun isMineOrWatched(transactionBag: TransactionBag): Boolean {
//        return isMine(transactionBag) || isWatched(transactionBag)
//    }

    /**
     * Returns true if this output is to a key, or an address we have the keys for, in the wallet.
     */
/*
    fun isWatched(transactionBag: TransactionBag): Boolean {
        try {
            val script = scriptPubKey
            return transactionBag.isWatchedScript(script)
        } catch (e: ScriptException) {
            // Just means we didn't understand the output of this transaction: ignore it.
            log.debug("Could not parse tx output script: {}", e.toString())
            return false
        }
    }
*/

    /**
     * Returns true if this output is to a key, or an address we have the keys for, in the wallet.
     */
/*
    fun isMine(transactionBag: TransactionBag): Boolean {
        try {
            val script = scriptPubKey
            if (script.isSentToRawPubKey) {
                val pubkey = script.pubKey
                return transactionBag.isPubKeyMine(pubkey)
            }
            if (script.isPayToScriptHash) {
                return transactionBag.isPayToScriptHashMine(script.pubKeyHash!!)
            } else {
                val pubkeyHash = script.pubKeyHash
                return transactionBag.isPubKeyHashMine(pubkeyHash!!)
            }
        } catch (e: ScriptException) {
            // Just means we didn't understand the output of this transaction: ignore it.
            log.debug("Could not parse tx {} output script: {}", if (parent != null) parent!!.hash else "(no parent)", e.toString())
            return false
        }
    }
*/

    /**
     * Returns a human readable debug string.
     */
/*
    override fun toString(): String {
        try {
            val script = scriptPubKey
            val buf = StringBuilder("TxOut of ")
            buf.append(Coin.valueOf(value).toFriendlyString())
            if (script.isSentToAddress || script.isPayToScriptHash)
                buf.append(" to ").append(script.getToAddress(params!!))
            else if (script.isSentToRawPubKey)
                buf.append(" to pubkey ").append(HEX.encode(script.pubKey))
            else if (script.isSentToMultiSig)
                buf.append(" to multisig")
            else
                buf.append(" (unknown type)")
            buf.append(" script:").append(script)
            return buf.toString()
        } catch (e: ScriptException) {
            throw RuntimeException(e)
        }

    }
*/

    /** Returns a copy of the output detached from its containing transaction, if need be.  */
/*
    fun duplicateDetached(): TransactionOutput {
        return TransactionOutput(params!!, null, Coin.valueOf(value), org.spongycastle.util.Arrays.clone(scriptBytes))
    }
*/

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        return if (o != null && o is TransactionOutput)
            value == o.value && Arrays.equals(scriptBytes, o.scriptBytes)
        else
            false
    }

    override fun hashCode(): Int {
        return Objects.hash(value, scriptBytes)
    }

    companion object {
        @JvmOverloads
        @JvmStatic
        @Throws(ProtocolException::class)
        fun parse(payload: ByteArray, offset: Int = 0): TransactionOutput {
            return parse(MessageReader(payload, offset))
        }

        @JvmStatic
        @Throws(ProtocolException::class)
        fun parse(reader: MessageReader): TransactionOutput {
            val value = reader.readInt64()
            val scriptLen = reader.readVarInt().toInt()
            val scriptBytes = reader.readBytes(scriptLen)
            return TransactionOutput(Coin.valueOf(value), scriptBytes)
        }


    }
}
