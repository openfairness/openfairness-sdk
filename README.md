
# OpenFairness SDK

A Java SDK providing blind box automatic allocation services and verifiable fairness algorithm unboxing services.

## 🎯 Project Introduction

OpenFairness SDK offers developers two core services:
- **Blind Box Automatic Allocation Service** - Intelligently configures blind box contents with multiple allocation strategies
- **Verifiable Fairness Algorithm Unboxing Service** - Ensures fairness and verifiability of unboxing process using cryptographic principles

## ✨ Core Features

- 🔒 **Verifiable Fairness** - Uses cryptographic algorithms to guarantee tamper-proof and verifiable unboxing results
- 🎁 **Smart Box Allocation** - Supports multiple blind box configuration strategies for various business scenarios
- 🚀 **Easy to Use** - Clean API design for quick integration into existing projects
- 📦 **Lightweight** - Minimal dependencies, optimized package size
- 🔧 **Highly Configurable** - Supports custom API endpoints, proxy configurations, and more

## 📦 Installation

### Maven Dependency
#### Environment Requirements
- Java 8+
- Maven 3.6+

```
<dependency>
    <groupId>com.openfairness</groupId>
    <artifactId>openfairness-sdk</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 🚀 Quick Start

### 1. Initialize the SDK

```java
// Method 1: Quick initialization with AppId (using default configuration)
OpenFairnessClient.build("your-app-id");

// Method 2: Initialize with complete configuration
RequestOptions options = RequestOptions.builder()
.apiUrl("https://api.openfairness.com")
.appId("your-app-id")
.build();
OpenFairnessClient.build(options);
```
### 2. Use Fairness Unboxing Service


```java
    OpenFairnessClient.build(RequestOptions.builder()
                    .appId("your-app-id").build());
    OpenFairnessResult result = OpenFairnessClient.openFairness(OpenFairnessReq.builder()
        .requestId(RandomStringUtils.randomAlphanumeric(32))
        .nonce(1)
        .boxDetails(Arrays.asList(
            OpenFairnessReq.BoxDetail.builder().rangeStart(1).rangeEnd(5_000_000).build(),
            OpenFairnessReq.BoxDetail.builder().rangeStart(5_000_001).rangeEnd(10_000_000).build(),
            OpenFairnessReq.BoxDetail.builder().rangeStart(10_000_001).rangeEnd(50_000_000).build(),
            OpenFairnessReq.BoxDetail.builder().rangeStart(50_000_001).rangeEnd(100_000_000).build()))
                    .build());
    System.out.println(JSON.toJSONString(result));
```

### 3. Use Blind Box Allocation Service

```java
try {
    OpenFairnessClient.build(RequestOptions.builder()
                    .appId("your-app-id").build());
    BoxMockReq req = BoxMockReq.builder().boxPrice(new BigDecimal(80)).temType("win_or_nothing").winRate(new BigDecimal(0.3)).build();
    List<BoxMockReq.BoxSku> skuList = new ArrayList<>();
    String rawData = "4025,3910,3876,2800,2650,2400,2250,2100,1850," +
        "1200,1100,950,808,750,690,600,514,488,450,400," +
        "339,300,250,200,160,150,130,120,109,99,88,78," +
        "60,51,45,38,29,25,18,12,8,5,1";
    String[] arr = rawData.split(",");
    int idx = 1;
    for (String s : arr) {
        BoxMockReq.BoxSku boxSku = new BoxMockReq.BoxSku();
                boxSku.setSkuNo("SKU_" + idx);
                boxSku.setGroupCode("SKU_" + idx);
                boxSku.setPrice(new BigDecimal(s));
        skuList.add(boxSku);
        idx++;
    }
    req.setSkuList(skuList);
    BoxMockResult result = OpenFairnessClient.boxMock(req);
    System.out.println(JSON.toJSONString(result));
} catch (Exception e) {
    System.out.println(e.getMessage());
}

```
## 🔧 API Documentation

### Core Classes

#### OpenFairnessClient
Main client class providing all service methods.

**Main Methods:**
- `build(RequestOptions)` - Initialize SDK
- `build(String appId)` - Quick initialization with AppId
- `openFairness(OpenFairnessReq)` - Fairness unboxing
- `boxMock(BoxMockReq)` - Blind box allocation
- `boxMockTypes()` - Get blind box type list

#### RequestOptions
Configuration options class supporting fluent interface.

**Configuration Items:**
- `apiUrl` - API service address (default: https://api.openfairness.com)
- `appId` - Application identifier (required)
- `proxyUrl` - Proxy address (optional)

### Exception Handling

All operations may throw `OpenFairnessException`, containing error code and message:

```java
try {
    OpenFairnessResult result = OpenFairnessClient.openFairness(request);
} catch (OpenFairnessException e) {
    System.err.println("Error code: " + e.getCode());
    System.err.println("Error message: " + e.getMessage());
}
```

## 🛡️ Fairness Verification

OpenFairness uses cryptographic hash algorithms to ensure fairness of unboxing results. Each unboxing result includes a verifiable hash that users can validate through:

1. **Get Verification Info** - Obtain verification hash from unboxing result
2. **Local Verification** - Use provided verification tools to validate result authenticity



