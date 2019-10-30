Domain or Subdomain takeover due to unclaimed S3 bucket is very easy to execute security vulnerability. An attacker can create a S3 bucket with the same name and in the same region which could host malicious content.

More details can be found on this blog post:
https://www.we45.com/blog/how-an-unclaimed-aws-s3-bucket-escalates-to-subdomain-takeover

# awss3security
This code finds R53 domains which are of type S3 ALIAS and configured to serve static content hosted on S3 and NOT pointing to ANY S3 buckets.
