/*
 * Copyright 2012 Google Inc.
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

package com.nchain.bitcoinkt.core

import com.nchain.params.NetworkParameters
import com.nchain.shared.ProtocolException
import java.util.ArrayList

/**
 *
 * Sent by a peer when a getdata request doesn't find the requested data in the mempool. It has the same format
 * as an inventory message and lists the hashes of the missing items.
 *
 *
 * Instances of this class are not safe for use by multiple threads.
 */
class NotFoundMessage : InventoryMessage {

    constructor(params: NetworkParameters) : super(params) {}

    @Throws(ProtocolException::class)
    constructor(params: NetworkParameters, payloadBytes: ByteArray) : super(params, payloadBytes) {
    }

    constructor(params: NetworkParameters, items: List<InventoryItem>) : super(params) {
        this.items = ArrayList(items)
    }

    companion object {
        var MIN_PROTOCOL_VERSION = 70001
    }
}
