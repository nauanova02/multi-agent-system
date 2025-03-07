import boto3
import click
from datetime import datetime, timedelta, timezone

# Initialize the CloudWatch client
cloudwatch = boto3.client('cloudwatch', region_name='eu-north-1')  # Replace 'your-region' with the correct AWS region

def get_ec2_metrics(instance_id):
    # Define the metrics you want to monitor
    metrics = [
        'CPUUtilization',    # CPU usage
        'DiskReadOps',       # Disk read operations
        'DiskWriteOps',      # Disk write operations
        'NetworkIn',         # Incoming network traffic
        'NetworkOut',        # Outgoing network traffic
        'DiskReadBytes',     # Bytes read from the disk
        'DiskWriteBytes',    # Bytes written to the disk
    ]
    
    # Time settings (last hour)
    end_time = datetime.now(timezone.utc)  # Use timezone-aware datetime
    start_time = end_time - timedelta(days=1)
    
    # Loop through each metric and fetch data
    for metric_name in metrics:
        response = cloudwatch.get_metric_statistics(
            Namespace='AWS/EC2',
            MetricName=metric_name,
            Dimensions=[
                {
                    'Name': 'InstanceId',
                    'Value': instance_id
                },
            ],
            StartTime=start_time,
            EndTime=end_time,
            Period=300,  # 5-minute intervals
            Statistics=['Average']
        )
        
        # Display the metrics
        print(f"\n--- {metric_name} ---")
        if 'Datapoints' in response and response['Datapoints']:
            for data_point in sorted(response['Datapoints'], key=lambda x: x['Timestamp']):
                print(f"Time: {data_point['Timestamp']}, Average: {data_point['Average']}")
        else:
            print("No data found for this metric. Try expanding the time range or ensure CloudWatch monitoring is enabled for this instance.")

@click.command()
@click.option('--instance-id', required=True, help='The EC2 instance ID to monitor')
def monitor_ec2(instance_id):
    """Monitor multiple metrics of an EC2 instance."""
    print(f"Monitoring metrics for EC2 instance: {instance_id}")
    get_ec2_metrics(instance_id)

if __name__ == '__main__':
    monitor_ec2()
