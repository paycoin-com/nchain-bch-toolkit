/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nchain.bitcoinkt.core.listeners

import com.nchain.bitcoinkt.core.*
import com.nchain.bitcoinkt.exception.VerificationException

/**
 * Listener interface for when we receive a new block that contains a relevant
 * transaction.
 */
interface TransactionReceivedInBlockListener {
    /**
     *
     * Called by the [BlockChain] when we receive a new block that contains a relevant transaction.
     *
     *
     * A transaction may be received multiple times if is included into blocks in parallel chains. The blockType
     * parameter describes whether the containing block is on the main/best chain or whether it's on a presently
     * inactive side chain.
     *
     *
     * The relativityOffset parameter is an arbitrary number used to establish an ordering between transactions
     * within the same block. In the case where full blocks are being downloaded, it is simply the index of the
     * transaction within that block. When Bloom filtering is in use, we don't find out the exact offset into a block
     * that a transaction occurred at, so the relativity count is not reflective of anything in an absolute sense but
     * rather exists only to order the transaction relative to the others.
     */
    @Throws(VerificationException::class)
    fun receiveFromBlock(tx: Transaction, block: StoredBlock?,
                         blockType: AbstractBlockChain.NewBlockType,
                         relativityOffset: Int)

    /**
     *
     * Called by the [BlockChain] when we receive a new [FilteredBlock] that contains the given
     * transaction hash in its merkle tree.
     *
     *
     * A transaction may be received multiple times if is included into blocks in parallel chains. The blockType
     * parameter describes whether the containing block is on the main/best chain or whether it's on a presently
     * inactive side chain.
     *
     *
     * The relativityOffset parameter in this case is an arbitrary (meaningless) number, that is useful only when
     * compared to the relativity count of another transaction received inside the same block. It is used to establish
     * an ordering of transactions relative to one another.
     *
     *
     * This method should return false if the given tx hash isn't known about, e.g. because the the transaction was
     * a Bloom false positive. If it was known about and stored, it should return true. The caller may need to know
     * this to calculate the effective FP rate.
     *
     * @return whether the transaction is known about i.e. was considered relevant previously.
     */
    @Throws(VerificationException::class)
    fun notifyTransactionIsInBlock(txHash: Sha256Hash, block: StoredBlock,
                                   blockType: AbstractBlockChain.NewBlockType,
                                   relativityOffset: Int): Boolean
}
