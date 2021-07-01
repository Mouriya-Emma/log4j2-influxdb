<h1 align="center">Welcome to log4j2-influxdb 👋</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-0.1-blue.svg?cacheSeconds=2592000" />
  <a href="https://opensource.org/licenses/BSD-3-Clause" target="_blank">
    <img alt="License: BSD--3--Clause" src="https://img.shields.io/badge/License-BSD--3--Clause-yellow.svg" />
  </a>
</p>

#### Output the log of your java project to Influxdb

## Dependence

log4j-core version need above to 2.11

## Getting Started

### add dependency

use Maven

```xml
<dependency>
  <groupId>io.github.mouriya-emma</groupId>
  <artifactId>log4j2-influxdb</artifactId>
  <version>0.1.1</version>
</dependency>
```

use gradle

```kotlin
  implementation("io.github.mouriya-emma:log4j2-influxdb:0.1.1")
```

or

```groovy
  implementation group: 'io.github.mouriya-emma', name: 'log4j2-influxdb', version: '0.1.1'
```

### add configuration in log4j xml 

```xml
...
<Appenders>
    ...
	<NoSql name="InfluxDb">
		<InfluxDb url="your_url" org="your_org" bucket="your_bucket" token="your_token" batchSize=10/>
	</NoSql>
    ...
</Appenders>
...
```

This library use InfluxDB Client asynchronous batch submission to insert data, in xml file configuration, will give it a default value 10 when `batchSize` attribute in the InfluxDb object is null.

When the number of logs submitted meets `batchSize` in InfluxDB Client local cache, the client would batch insert logs.

If you need logs to be inserted into the database in real time, synchronization will be provided in the future.

## Development Plan

Unfinished

* Custom Layout
* Synchronization insert




## 📝 License

This project is [BSD--3--Clause](https://opensource.org/licenses/BSD-3-Clause) licensed.

***
_This README was generated with ❤️ by [readme-md-generator](https://github.com/kefranabg/readme-md-generator)_
