#include "ros/ros.h"
#include<geometry_msgs/Twist.h> // motion speed structure type geometry_msgs::Twist definition file
 
int main(int argc, char *argv[])
{
    ros::init(argc, argv, "vel_ctrl");  //Initialize the node
    ros::NodeHandle n;         //Declare a NodeHandle object n and generate a broadcast object vel with n_pub
    ros::Publisher vel_pub = n.advertise<geometry_msgs::Twist>("/turtle1/cmd_vel", 10);
    //vel_pub will broadcast geometry in the topic "/cmd_vel" (robot speed control topic)_ Msgs:: twist type data
    ROS_INFO("draw_circle start...");//Output display information
    while(ros::ok())
    {
        geometry_msgs::Twist vel_cmd; //Declare a geometry_ Msgs:: object vel of type twist_ CMD and assign the value of speed to this object
 
        vel_cmd.linear.x = 2.0;  //Front and rear (+ -) m/s
        vel_cmd.linear.y = 0.0;  //Left and right (+ -) m/s
        vel_cmd.linear.z = 0.0;
 
        vel_cmd.angular.x = 0;
        vel_cmd.angular.y = 0;
        vel_cmd.angular.z = 1.8; //Rotation speed of robot, + left turn, - right turn, unit: rad/s
        vel_pub.publish(vel_cmd); //After assignment, it is sent to the topic "/ cmd_vel". The core node of the robot will receive and send the past speed value from this topic and forward it to the hardware body for execution
 
        ros::spinOnce();//Calling this function allows other callback functions to be executed (more than the routine does not use callback functions)
    }
    return 0;
}
