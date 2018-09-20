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
import com.nchain.bitcoinkt.core.PeerAddress

/**
 *
 * Implementors can listen to events for peers being discovered.
 */
interface PeerDiscoveredEventListener {
    /**
     *
     * Called when peers are discovered, this happens at startup of [PeerGroup] or if we run out of
     * suitable [Peer]s to connect to.
     *
     * @param peerAddresses the set of discovered [PeerAddress]es
     */
    fun onPeersDiscovered(peerAddresses: Set<PeerAddress>)
}
