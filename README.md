Domain or Subdomain takeover due to unclaimed S3 bucket is very easy to execute security vulnerability. An attacker can create a S3 bucket with the same name and in the same region which could host malicious content.

More details can be found on this blog post:
https://www.we45.com/blog/how-an-unclaimed-aws-s3-bucket-escalates-to-subdomain-takeover

# awss3security
This code finds R53 domains which are of type S3 ALIAS and configured to serve static content hosted on S3 and NOT pointing to ANY S3 buckets.

#How it works?
This is simple READ ONLY code which enumerates through the R53 record sets and compares them with S3 buckets. If any domain records which are NOT associated with S3 buckets are found, they will be listed on console.

#How to run?
For users who wouldn't want to check-out and compile the code, there is a comiled .jar is available under "target" folder.

#Step1
Download the .jar file to any EC2 machine which has a policy associated with permissions to read R53 and S3 buckets

#Step2
java -jar <jarfilename>

#Prerequistes
JRE 8
EC2 machine with R53 and S3 policies

Submit a github issue (or) pull request, if you need any further functionality.
