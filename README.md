# MDMSample
a simple android mobile device management example

  The project aims to create a device admin application. It consists of an DeviceAdminReceiver class, 
a Service class and an Activity. After Activity launched, device admin permission is checked immediately.
If the application has not been given device admin permission, it asks the user for the permission. Otherwise,
after MDMService object started, Activity launches and main screen is shown to the user.

  A user has three options; enable camera, disable camera and start an activity (Youtube will be choosen in our example).
In order to achive these tasks, a service connection will be established to our MDMService and try to bind the service. 
After service bounded to our activity, user will be able to achive tasks by clicking related buttons. For each button 
click event, apropriate service method will be called and it will achive the task in new thread.
