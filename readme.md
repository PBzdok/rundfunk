# Rundfunk
Personal Discord bot featuring [Discord4J](https://github.com/Discord4J/Discord4J) and [Lavaplayer(Fork)](https://github.com/Walkyst/lavaplayer-fork/releases)

## Local setup
`$ ./gradlew build`

`$ ./gradlew run --args=<token>`

## Docker Setup
`$ docker build -t rundfunk:1.0 .`

`$ docker run rundfunk:1.0 <token>` (optional)

## Register as Service
```
# /etc/systemd/system/rundfunk.service
[Unit]
Description=Rundfunk Discord Server
After=docker.service
Requires=docker.service

[Service]
Type=simple
Restart=always
User=root
Group=root

ExecStart=/usr/bin/docker run rundfunk:1.0 <token>

[Install]
WantedBy=multi-user.target
```

## Use as Service

`$ systemctl enable rundfunk.service`

`$ systemctl start rundfunk.service`
