/*
 * Nextcloud Talk - Android Client
 *
 * SPDX-FileCopyrightText: 2022-2024 Marcel Hibbe <dev@mhibbe.de>
 * SPDX-FileCopyrightText: 2022 Andy Scherzinger <info@andy-scherzinger.de>
 * SPDX-FileCopyrightText: 2022 Álvaro Brey <alvaro@alvarobrey.com>
 * SPDX-FileCopyrightText: 2022 Nextcloud GmbH
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.nextcloud.talk.dagger.modules

import com.nextcloud.talk.api.NcApi
import com.nextcloud.talk.api.NcApiCoroutines
import com.nextcloud.talk.chat.data.ChatMessageRepository
import com.nextcloud.talk.chat.data.network.ChatNetworkDataSource
import com.nextcloud.talk.chat.data.network.OfflineFirstChatRepository
import com.nextcloud.talk.chat.data.network.RetrofitChatNetwork
import com.nextcloud.talk.contacts.ContactsRepository
import com.nextcloud.talk.contacts.ContactsRepositoryImpl
import com.nextcloud.talk.conversation.repository.ConversationRepository
import com.nextcloud.talk.conversation.repository.ConversationRepositoryImpl
import com.nextcloud.talk.conversationinfoedit.data.ConversationInfoEditRepository
import com.nextcloud.talk.conversationinfoedit.data.ConversationInfoEditRepositoryImpl
import com.nextcloud.talk.conversationlist.data.OfflineConversationsRepository
import com.nextcloud.talk.conversationlist.data.network.ConversationsNetworkDataSource
import com.nextcloud.talk.conversationlist.data.network.OfflineFirstConversationsRepository
import com.nextcloud.talk.conversationlist.data.network.RetrofitConversationsNetwork
import com.nextcloud.talk.data.database.dao.ChatBlocksDao
import com.nextcloud.talk.data.database.dao.ChatMessagesDao
import com.nextcloud.talk.data.database.dao.ConversationsDao
import com.nextcloud.talk.data.network.NetworkMonitor
import com.nextcloud.talk.data.source.local.TalkDatabase
import com.nextcloud.talk.data.storage.ArbitraryStoragesRepository
import com.nextcloud.talk.data.storage.ArbitraryStoragesRepositoryImpl
import com.nextcloud.talk.data.user.UsersRepository
import com.nextcloud.talk.data.user.UsersRepositoryImpl
import com.nextcloud.talk.invitation.data.InvitationsRepository
import com.nextcloud.talk.invitation.data.InvitationsRepositoryImpl
import com.nextcloud.talk.openconversations.data.OpenConversationsRepository
import com.nextcloud.talk.openconversations.data.OpenConversationsRepositoryImpl
import com.nextcloud.talk.polls.repositories.PollRepository
import com.nextcloud.talk.polls.repositories.PollRepositoryImpl
import com.nextcloud.talk.raisehand.RequestAssistanceRepository
import com.nextcloud.talk.raisehand.RequestAssistanceRepositoryImpl
import com.nextcloud.talk.remotefilebrowser.repositories.RemoteFileBrowserItemsRepository
import com.nextcloud.talk.remotefilebrowser.repositories.RemoteFileBrowserItemsRepositoryImpl
import com.nextcloud.talk.repositories.callrecording.CallRecordingRepository
import com.nextcloud.talk.repositories.callrecording.CallRecordingRepositoryImpl
import com.nextcloud.talk.repositories.conversations.ConversationsRepository
import com.nextcloud.talk.repositories.conversations.ConversationsRepositoryImpl
import com.nextcloud.talk.repositories.reactions.ReactionsRepository
import com.nextcloud.talk.repositories.reactions.ReactionsRepositoryImpl
import com.nextcloud.talk.repositories.unifiedsearch.UnifiedSearchRepository
import com.nextcloud.talk.repositories.unifiedsearch.UnifiedSearchRepositoryImpl
import com.nextcloud.talk.shareditems.repositories.SharedItemsRepository
import com.nextcloud.talk.shareditems.repositories.SharedItemsRepositoryImpl
import com.nextcloud.talk.translate.repositories.TranslateRepository
import com.nextcloud.talk.translate.repositories.TranslateRepositoryImpl
import com.nextcloud.talk.users.UserManager
import com.nextcloud.talk.utils.DateUtils
import com.nextcloud.talk.utils.database.user.CurrentUserProviderNew
import com.nextcloud.talk.utils.preferences.AppPreferences
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class RepositoryModule {

    @Provides
    fun provideConversationsRepository(ncApi: NcApi, userProvider: CurrentUserProviderNew): ConversationsRepository {
        return ConversationsRepositoryImpl(ncApi, userProvider)
    }

    @Provides
    fun provideSharedItemsRepository(ncApi: NcApi, dateUtils: DateUtils): SharedItemsRepository {
        return SharedItemsRepositoryImpl(ncApi, dateUtils)
    }

    @Provides
    fun provideUnifiedSearchRepository(ncApi: NcApi, userProvider: CurrentUserProviderNew): UnifiedSearchRepository {
        return UnifiedSearchRepositoryImpl(ncApi, userProvider)
    }

    @Provides
    fun provideDialogPollRepository(ncApi: NcApi, userProvider: CurrentUserProviderNew): PollRepository {
        return PollRepositoryImpl(ncApi, userProvider)
    }

    @Provides
    fun provideRemoteFileBrowserItemsRepository(
        okHttpClient: OkHttpClient,
        userProvider: CurrentUserProviderNew
    ): RemoteFileBrowserItemsRepository {
        return RemoteFileBrowserItemsRepositoryImpl(okHttpClient, userProvider)
    }

    @Provides
    fun provideUsersRepository(database: TalkDatabase): UsersRepository {
        return UsersRepositoryImpl(database.usersDao())
    }

    @Provides
    fun provideArbitraryStoragesRepository(database: TalkDatabase): ArbitraryStoragesRepository {
        return ArbitraryStoragesRepositoryImpl(database.arbitraryStoragesDao())
    }

    @Provides
    fun provideReactionsRepository(
        ncApi: NcApi,
        userProvider: CurrentUserProviderNew,
        dao: ChatMessagesDao
    ): ReactionsRepository {
        return ReactionsRepositoryImpl(ncApi, userProvider, dao)
    }

    @Provides
    fun provideCallRecordingRepository(ncApi: NcApi, userProvider: CurrentUserProviderNew): CallRecordingRepository {
        return CallRecordingRepositoryImpl(ncApi, userProvider)
    }

    @Provides
    fun provideRequestAssistanceRepository(
        ncApi: NcApi,
        userProvider: CurrentUserProviderNew
    ): RequestAssistanceRepository {
        return RequestAssistanceRepositoryImpl(ncApi, userProvider)
    }

    @Provides
    fun provideOpenConversationsRepository(
        ncApi: NcApi,
        userProvider: CurrentUserProviderNew
    ): OpenConversationsRepository {
        return OpenConversationsRepositoryImpl(ncApi, userProvider)
    }

    @Provides
    fun translateRepository(ncApi: NcApi): TranslateRepository {
        return TranslateRepositoryImpl(ncApi)
    }

    @Provides
    fun provideChatNetworkDataSource(ncApi: NcApi): ChatNetworkDataSource {
        return RetrofitChatNetwork(ncApi)
    }

    @Provides
    fun provideConversationsNetworkDataSource(ncApi: NcApi): ConversationsNetworkDataSource {
        return RetrofitConversationsNetwork(ncApi)
    }

    @Provides
    fun provideConversationInfoEditRepository(
        ncApi: NcApi,
        userProvider: CurrentUserProviderNew
    ): ConversationInfoEditRepository {
        return ConversationInfoEditRepositoryImpl(ncApi, userProvider)
    }

    @Provides
    fun provideConversationRepository(ncApi: NcApi, userProvider: CurrentUserProviderNew): ConversationRepository {
        return ConversationRepositoryImpl(ncApi, userProvider)
    }

    @Provides
    fun provideInvitationsRepository(ncApi: NcApi): InvitationsRepository {
        return InvitationsRepositoryImpl(ncApi)
    }

    @Provides
    fun provideOfflineFirstChatRepository(
        chatMessagesDao: ChatMessagesDao,
        chatBlocksDao: ChatBlocksDao,
        dataSource: ChatNetworkDataSource,
        appPreferences: AppPreferences,
        networkMonitor: NetworkMonitor,
        userProvider: CurrentUserProviderNew
    ): ChatMessageRepository {
        return OfflineFirstChatRepository(
            chatMessagesDao,
            chatBlocksDao,
            dataSource,
            appPreferences,
            networkMonitor,
            userProvider
        )
    }

    @Provides
    fun provideOfflineFirstConversationsRepository(
        dao: ConversationsDao,
        dataSource: ConversationsNetworkDataSource,
        chatNetworkDataSource: ChatNetworkDataSource,
        networkMonitor: NetworkMonitor,
        currentUserProviderNew: CurrentUserProviderNew
    ): OfflineConversationsRepository {
        return OfflineFirstConversationsRepository(
            dao,
            dataSource,
            chatNetworkDataSource,
            networkMonitor,
            currentUserProviderNew
        )
    }

    @Provides
    fun provideContactsRepository(ncApiCoroutines: NcApiCoroutines, userManager: UserManager): ContactsRepository {
        return ContactsRepositoryImpl(ncApiCoroutines, userManager)
    }
}
