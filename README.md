<img src="http://i.imgur.com/hXY4lcC.png" height="42px" alt="microG" /> Services Core (GmsCore)
=======

A 1:1 version of [microG](https://github.com/microg/GmsCore) designed for ReVanced apps.

A big thanks to [Shadow578](https://github.com/shadow578) for its first implementation.

## Notable changes

- No longer a system app
- Package name changed from `com.google.android.gms` to `com.mgoogle.android.gms` to support installation alongside the official MicroG
- Removed unnecessary features:
  - Ads
  - Analytics
  - Car
  - Droidguard
  - Exposure-Notifications
  - Feedback
  - Firebase
  - Games
  - Maps
  - Recovery
  - SafetyNet
  - Search
  - TapAndPay
  - Wallet
  - Wear-Api
- Removed all permissions, as none are required for Google authentication
