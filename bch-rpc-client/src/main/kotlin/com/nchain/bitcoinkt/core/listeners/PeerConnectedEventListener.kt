/*
 * Copyright 2011 Google Inc.
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

package com.nchain.bitcoinkt.core.listeners

import com.nchain.bitcoinkt.core.Peer

/**
 *
 * Implementors can listen to events indicating a new peer connecting.
 */
interface PeerConnectedEventListener {

    /**
     * Called when a peer is connected. If this listener is registered to a [Peer] instead of a [PeerGroup],
     * peerCount will always be 1.
     *
     * @param peer
     * @param peerCount the total number of connected peers
     */
    fun onPeerConnected(peer: Peer, peerCount: Int)
}
