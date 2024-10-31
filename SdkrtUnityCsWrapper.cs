
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class SDKRTTest : MonoBehaviour
{
    public TextMeshProUGUI textToChange;
    private bool changed = false;

    public void LoadSdk()
    {
        // Get the current Android Activity
        AndroidJavaClass unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject currentActivity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");

        // Create an instance of UnitySdkBridge class
        AndroidJavaObject existingSdkObject = new AndroidJavaObject("com.example.unitybridge.UnitySdkBridge", currentActivity);

        // Create an Action that will handle the callback from Kotlin
        System.Action<bool> callback = (result) => {
            if (result) {
                textToChange.text = "SDK initialised.";
            } else {
                textToChange.text = "SDK not initialised.";
            }
        };

        AndroidJavaProxy callbackProxy = new InitializationCallbackProxy(callback);

        // Call the initializeSync method with the runnable - Name needs to match
        existingSdkObject.Call("initializeSync", callbackProxy);
    }

    public void CreateFile()
    {
        // Get the current Android Activity
        AndroidJavaClass unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject currentActivity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");

        // Create an instance of UnitySdkBridge class
        AndroidJavaObject existingSdkObject = new AndroidJavaObject("com.example.unitybridge.UnitySdkBridge", currentActivity);

        // Create an Action that will handle the callback from Kotlin
        System.Action<string> callback = (result) => {
            textToChange.text = result;
        };

        AndroidJavaProxy callbackProxy = new CreateFileCallbackProxy(callback);

        // Call the initializeSync method with the runnable
        existingSdkObject.Call("createFileSync", 2, callbackProxy);
    }
}

public class InitializationCallbackProxy : AndroidJavaProxy
{
    private System.Action<bool> _callback;

    public InitializationCallbackProxy(System.Action<bool> callback)
        : base("com.example.unitybridge.InitializationCallback")
    {
        _callback = callback;
    }

    public void onInitializationComplete(bool result)
    {
        Debug.Log("and the result is: " + result);
        _callback(result);
    }
}

public class CreateFileCallbackProxy : AndroidJavaProxy
{
    private System.Action<string> _callback;

    public CreateFileCallbackProxy(System.Action<string> callback)
        : base("com.example.unitybridge.FileCreationCallback")
    {
        _callback = callback;
    }

    public void onFileCreationComplete(string result)
    {
        Debug.Log("and the result is: " + result);
        _callback(result);
    }
}