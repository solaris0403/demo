@file:Suppress("unused")

package com.huaxia.xlib.os

import android.accessibilityservice.AccessibilityService
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.AppOpsManager
import android.app.DownloadManager
import android.app.KeyguardManager
import android.app.NotificationManager
import android.app.SearchManager
import android.app.UiModeManager
import android.app.WallpaperManager
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.app.usage.NetworkStatsManager
import android.app.usage.StorageStatsManager
import android.app.usage.UsageStatsManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.companion.CompanionDeviceManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.ACCESSIBILITY_SERVICE
import android.content.Context.ACCOUNT_SERVICE
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.ALARM_SERVICE
import android.content.Context.APPWIDGET_SERVICE
import android.content.Context.APP_OPS_SERVICE
import android.content.Context.AUDIO_SERVICE
import android.content.Context.BATTERY_SERVICE
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Context.CAMERA_SERVICE
import android.content.Context.CAPTIONING_SERVICE
import android.content.Context.CARRIER_CONFIG_SERVICE
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.COMPANION_DEVICE_SERVICE
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Context.CONSUMER_IR_SERVICE
import android.content.Context.CROSS_PROFILE_APPS_SERVICE
import android.content.Context.DEVICE_POLICY_SERVICE
import android.content.Context.DISPLAY_SERVICE
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Context.DROPBOX_SERVICE
import android.content.Context.EUICC_SERVICE
import android.content.Context.FINGERPRINT_SERVICE
import android.content.Context.HARDWARE_PROPERTIES_SERVICE
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.INPUT_SERVICE
import android.content.Context.IPSEC_SERVICE
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Context.KEYGUARD_SERVICE
import android.content.Context.LAUNCHER_APPS_SERVICE
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.LOCATION_SERVICE
import android.content.Context.MEDIA_PROJECTION_SERVICE
import android.content.Context.MEDIA_ROUTER_SERVICE
import android.content.Context.MEDIA_SESSION_SERVICE
import android.content.Context.MIDI_SERVICE
import android.content.Context.NETWORK_STATS_SERVICE
import android.content.Context.NFC_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Context.NSD_SERVICE
import android.content.Context.POWER_SERVICE
import android.content.Context.PRINT_SERVICE
import android.content.Context.RESTRICTIONS_SERVICE
import android.content.Context.SEARCH_SERVICE
import android.content.Context.SENSOR_SERVICE
import android.content.Context.SHORTCUT_SERVICE
import android.content.Context.STORAGE_SERVICE
import android.content.Context.STORAGE_STATS_SERVICE
import android.content.Context.SYSTEM_HEALTH_SERVICE
import android.content.Context.TELECOM_SERVICE
import android.content.Context.TELEPHONY_SERVICE
import android.content.Context.TELEPHONY_SUBSCRIPTION_SERVICE
import android.content.Context.TEXT_CLASSIFICATION_SERVICE
import android.content.Context.TEXT_SERVICES_MANAGER_SERVICE
import android.content.Context.TV_INPUT_SERVICE
import android.content.Context.UI_MODE_SERVICE
import android.content.Context.USAGE_STATS_SERVICE
import android.content.Context.USB_SERVICE
import android.content.Context.USER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.content.Context.WALLPAPER_SERVICE
import android.content.Context.WIFI_AWARE_SERVICE
import android.content.Context.WIFI_P2P_SERVICE
import android.content.Context.WIFI_RTT_RANGING_SERVICE
import android.content.Context.WIFI_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.content.RestrictionsManager
import android.content.pm.CrossProfileApps
import android.content.pm.LauncherApps
import android.content.pm.ShortcutManager
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.input.InputManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.media.midi.MidiManager
import android.media.projection.MediaProjectionManager
import android.media.session.MediaSessionManager
import android.media.tv.TvInputManager
import android.net.ConnectivityManager
import android.net.IpSecManager
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.aware.WifiAwareManager
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.rtt.WifiRttManager
import android.nfc.NfcManager
import android.os.BatteryManager
import android.os.DropBoxManager
import android.os.HardwarePropertiesManager
import android.os.PowerManager
import android.os.UserManager
import android.os.Vibrator
import android.os.health.SystemHealthManager
import android.os.storage.StorageManager
import android.print.PrintManager
import android.telecom.TelecomManager
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.inputmethod.InputMethodManager
import android.view.textclassifier.TextClassificationManager
import android.view.textservice.TextServicesManager
import androidx.annotation.RequiresApi
import com.huaxia.xlib.appctx.appCtx

inline val AccessibilityService.windowManager
    get() = getSystemService(WINDOW_SERVICE) as WindowManager
inline val Context.windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager
inline val View.windowManager get() = context.windowManager
inline val windowManager: WindowManager get() = getSystemService(WINDOW_SERVICE)
inline val Context.layoutInflater: LayoutInflater
    get() = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
inline val View.layoutInflater: LayoutInflater get() = context.layoutInflater
inline val activityManager: ActivityManager get() = getSystemService(ACTIVITY_SERVICE)
inline val powerManager: PowerManager get() = getSystemService(POWER_SERVICE)
inline val alarmManager: AlarmManager get() = getSystemService(ALARM_SERVICE)
inline val notificationManager: NotificationManager get() = getSystemService(NOTIFICATION_SERVICE)
inline val keyguardManager: KeyguardManager get() = getSystemService(KEYGUARD_SERVICE)
inline val locationManager: LocationManager get() = getSystemService(LOCATION_SERVICE)
inline val searchManager: SearchManager get() = getSystemService(SEARCH_SERVICE)
inline val sensorManager: SensorManager get() = getSystemService(SENSOR_SERVICE)
inline val storageManager: StorageManager get() = getSystemService(STORAGE_SERVICE)

/** Null if invoked in an instant app. */
inline val wallpaperManager: WallpaperManager? get() = getSystemService(WALLPAPER_SERVICE)
inline val vibrator: Vibrator get() = getSystemService(VIBRATOR_SERVICE)
inline val connectivityManager: ConnectivityManager get() = getSystemService(CONNECTIVITY_SERVICE)

/** Null if invoked in an instant app. */
inline val wifiManager: WifiManager?
    @SuppressLint("WifiManagerLeak") get() = getSystemService(WIFI_SERVICE)

/** Null if invoked in an instant app. */
inline val wifiP2pManager: WifiP2pManager? get() = getSystemService(WIFI_P2P_SERVICE)
inline val audioManager: AudioManager get() = getSystemService(AUDIO_SERVICE)
inline val telephonyManager: TelephonyManager get() = getSystemService(TELEPHONY_SERVICE)
inline val inputMethodManager: InputMethodManager get() = getSystemService(INPUT_METHOD_SERVICE)
inline val downloadManager: DownloadManager get() = getSystemService(DOWNLOAD_SERVICE)
inline val uiModeManager: UiModeManager get() = getSystemService(UI_MODE_SERVICE)

/** Null if invoked in an instant app. */
inline val usbManager: UsbManager? get() = getSystemService(USB_SERVICE)
inline val nfcManager: NfcManager get() = getSystemService(NFC_SERVICE)

/** Null if invoked in an instant app. */
inline val devicePolicyManager: DevicePolicyManager? get() = getSystemService(DEVICE_POLICY_SERVICE)
inline val textServicesManager: TextServicesManager
    get() = getSystemService(TEXT_SERVICES_MANAGER_SERVICE)
inline val clipboardManager: ClipboardManager get() = getSystemService(CLIPBOARD_SERVICE)
inline val accessibilityManager: AccessibilityManager
    get() = getSystemService(ACCESSIBILITY_SERVICE)
inline val accountManager: AccountManager get() = getSystemService(ACCOUNT_SERVICE)
inline val dropBoxManager: DropBoxManager get() = getSystemService(DROPBOX_SERVICE)

inline val nsdManager: NsdManager get() = getSystemService(NSD_SERVICE)
inline val mediaRouter: MediaRouter get() = getSystemService(MEDIA_ROUTER_SERVICE)
inline val inputManager: InputManager get() = getSystemService(INPUT_SERVICE)

inline val displayManager: DisplayManager get() = getSystemService(DISPLAY_SERVICE)
inline val userManager: UserManager get() = getSystemService(USER_SERVICE)

inline val bluetoothManager: BluetoothManager get() = getSystemService(BLUETOOTH_SERVICE)

inline val appOpsManager: AppOpsManager get() = getSystemService(APP_OPS_SERVICE)
inline val printManager: PrintManager get() = getSystemService(PRINT_SERVICE)
inline val consumerIrManager: ConsumerIrManager get() = getSystemService(CONSUMER_IR_SERVICE)
inline val captioningManager: CaptioningManager get() = getSystemService(CAPTIONING_SERVICE)

inline val appWidgetManager: AppWidgetManager
    @RequiresApi(21) get() = getSystemService(APPWIDGET_SERVICE)
inline val mediaSessionManager: MediaSessionManager
    @RequiresApi(21) get() = getSystemService(MEDIA_SESSION_SERVICE)
inline val telecomManager: TelecomManager @RequiresApi(21) get() = getSystemService(TELECOM_SERVICE)
inline val launcherApps: LauncherApps
    @RequiresApi(21) get() = getSystemService(LAUNCHER_APPS_SERVICE)
inline val restrictionsManager: RestrictionsManager
    @RequiresApi(21) get() = getSystemService(RESTRICTIONS_SERVICE)
inline val cameraManager: CameraManager @RequiresApi(21) get() = getSystemService(CAMERA_SERVICE)
inline val tvInputManager: TvInputManager
    @RequiresApi(21)
    get() = getSystemService(TV_INPUT_SERVICE)
inline val batteryManager: BatteryManager @RequiresApi(21) get() = getSystemService(BATTERY_SERVICE)
inline val jobScheduler: JobScheduler
    @RequiresApi(21) get() = getSystemService(JOB_SCHEDULER_SERVICE)
inline val mediaProjectionManager: MediaProjectionManager
    @RequiresApi(21) get() = getSystemService(MEDIA_PROJECTION_SERVICE)

inline val usageStatsManager: UsageStatsManager
    @RequiresApi(22) get() = getSystemService(USAGE_STATS_SERVICE)
inline val subscriptionManager: SubscriptionManager
    @RequiresApi(22) get() = getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE)

/** Null if invoked in an instant app. */
@Suppress("DeprecatedCallableAddReplaceWith", "DEPRECATION")
@Deprecated("Use android.hardware.biometrics.BiometricPrompt instead (back-ported into JetPack).")
inline val fingerPrintManager: android.hardware.fingerprint.FingerprintManager?
    @RequiresApi(23) get() = getSystemService(FINGERPRINT_SERVICE)

inline val networkStatsManager: NetworkStatsManager
    @RequiresApi(23) get() = getSystemService(NETWORK_STATS_SERVICE)
inline val carrierConfigManager: CarrierConfigManager
    @RequiresApi(23) get() = getSystemService(CARRIER_CONFIG_SERVICE)
inline val midiManager: MidiManager @RequiresApi(23) get() = getSystemService(MIDI_SERVICE)

inline val hardwarePropertiesManager: HardwarePropertiesManager
    @RequiresApi(24) get() = getSystemService(HARDWARE_PROPERTIES_SERVICE)
inline val systemHealthManager: SystemHealthManager
    @RequiresApi(24) get() = getSystemService(SYSTEM_HEALTH_SERVICE)

/** Null if invoked in an instant app. */
inline val shortcutManager: ShortcutManager?
    @RequiresApi(25) get() = getSystemService(SHORTCUT_SERVICE)

inline val companionDeviceManager: CompanionDeviceManager
    @RequiresApi(26) get() = getSystemService(COMPANION_DEVICE_SERVICE)
inline val storageStatsManager: StorageStatsManager
    @RequiresApi(26) get() = getSystemService(STORAGE_STATS_SERVICE)
inline val textClassificationManager: TextClassificationManager
    @RequiresApi(26) get() = getSystemService(TEXT_CLASSIFICATION_SERVICE)

/** Null if invoked in an instant app. */
inline val wifiAwareManager: WifiAwareManager?
    @RequiresApi(26) get() = getSystemService(WIFI_AWARE_SERVICE)

inline val crossProfileApps: CrossProfileApps
    @RequiresApi(28) get() = getSystemService(CROSS_PROFILE_APPS_SERVICE)
inline val euiccManager: EuiccManager @RequiresApi(28) get() = getSystemService(EUICC_SERVICE)
inline val ipSecManager: IpSecManager @RequiresApi(28) get() = getSystemService(IPSEC_SERVICE)
inline val wifiRttManager: WifiRttManager
    @RequiresApi(28) get() = getSystemService(WIFI_RTT_RANGING_SERVICE)

@Suppress("UNCHECKED_CAST")
fun <T> getSystemService(name: String) = appCtx.getSystemService(name) as T
