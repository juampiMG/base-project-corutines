package com.jp.data.network.gateway.retrofit

import com.jp.data.network.gateway.IAppAuthGateway
import com.jp.data.preferences.URLPreferenceManager
import com.jp.data.network.gateway.retrofit.service.IAuthRestServices

class AppAuthGateway(var mService: IAuthRestServices, var urlPreferenceManager: URLPreferenceManager) : IAppAuthGateway {

}