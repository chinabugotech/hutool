<p align="center">
    <a href="https://hutool.cn/"><img src="https://cdn.jsdelivr.net/gh/looly/hutool-site/images/logo.jpg" width="45%"></a>
</p>
<p align="center">
    <strong>🍬A set of tools that keep Java sweet.</strong>
</p>
<p align="center">
    👉 <a href="https://hutool.cn">https://hutool.cn/</a> 👈
</p>

<p align="center">
    <a target="_blank" href="https://search.maven.org/artifact/cn.hutool/hutool-all">
        <img src="https://img.shields.io/maven-central/v/cn.hutool/hutool-all.svg?label=Maven%20Central" />
    </a>
    <a target="_blank" href="https://license.coscl.org.cn/MulanPSL2">
        <img src="https://img.shields.io/:license-MulanPSL2-blue.svg" />
    </a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
        <img src="https://img.shields.io/badge/JDK-8+-green.svg" />
    </a>
    <a target="_blank" href="https://app.travis-ci.com/chinabugotech/hutool">
        <img src="https://api.travis-ci.com/chinabugotech/hutool.svg?branch=v5-master" />
    </a>
    <a href="https://www.codacy.com/gh/chinabugotech/hutool/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=chinabugotech/hutool&amp;utm_campaign=Badge_Grade">
        <img src="https://app.codacy.com/project/badge/Grade/8a6897d9de7440dd9de8804c28d2871d"/>
    </a>
    <a href="https://codecov.io/gh/chinabugotech/hutool">
        <img src="https://codecov.io/gh/chinabugotech/hutool/branch/v5-master/graph/badge.svg" />
    </a>
    <a target="_blank" href="https://gitter.im/hutool/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge">
        <img src="https://badges.gitter.im/hutool/Lobby.svg" />
    </a>
    <a target="_blank" href='https://gitee.com/chinabugotech/hutool/stargazers'>
        <img src='https://gitee.com/chinabugotech/hutool/badge/star.svg?theme=gvp' alt='star'/>
    </a>
    <a target="_blank" href='https://github.com/chinabugotech/hutool'>
        <img src="https://img.shields.io/github/stars/chinabugotech/hutool.svg?style=social" alt="github star"/>
    </a>
    <a target="_blank" href='https://gitcode.com/chinabugotech/hutool'>
        <img src="https://gitcode.com/chinabugotech/hutool/star/badge.svg" alt="gitcode star"/>
    </a>
    <a href="https://deepwiki.com/chinabugotech/hutool">
        <img src="https://deepwiki.com/badge.svg" alt="Ask DeepWiki">
    </a>
</p>

<br/>
<p align="center">
    <a href="https://qm.qq.com/q/YsLhyheHSA">
    <img alt="" src="https://img.shields.io/badge/QQ%E7%BE%A4%E2%91%A7-667030118-orange"/></a>
</p>

-------------------------------------------------------------------------------

[**🌎中文说明**](README.md)

-------------------------------------------------------------------------------

## 📚Introduction

**Hutool** is a feature-rich and easy-to-use Java utility library. It provides a large set of static utility methods that help developers complete common tasks quickly and conveniently. The encapsulated tools cover strings, numbers, collections, encoding, dates, files, I/O, encryption, database (JDBC), JSON, HTTP clients, and more, meeting a broad range of development needs.

### 🎁Origin of the 'Hutool' name

Hutool = Hu + tool. It is an open-source library derived from the low-level code of the original company's projects, where "Hu" is the company name and "tool" stands for tools. "Hutool" is a homophone of "hútú" (糊涂), which is easy to remember and also hints at the Chinese idiom "难得糊涂" (roughly, "a rare blessing to stay blissfully unaware").

### 🍺Philosophy

**Hutool** is both a toolbox and a knowledge base. We never claim the code is entirely original — most utility classes are collected from elsewhere, so:

- You are free to use, copy, and modify it without any attribution. We only ask that you report bugs promptly.
- We make a constant effort to keep the comments comprehensive and in Chinese, so that anyone reading the source code can understand it.

The goal of **Hutool** is to replace a complex block of code with a single, simple method, avoiding copy-paste programming as much as possible and changing the way we write code.

Take calculating an MD5 hash as an example:

- 👴【Before】Open a search engine -> search "Java MD5 encryption" -> open a blog -> copy and paste -> tweak it until it works.
- 👦【Now】Import Hutool -> `SecureUtil.md5()`

Hutool exists to reduce the cost of searching for code and to avoid the bugs caused by imperfect snippets found online.

### Thanks
> This README is based on a PR by [chengxian-yi](https://gitee.com/yichengxian).

-------------------------------------------------------------------------------

## 🛠️Modules

A Java utility library that wraps the JDK's file, stream, encryption/decryption, transcoding, regex, thread, XML, and other APIs into various `Util` classes, and also provides the following modules:

| module             | description                                                                                                    |
|--------------------|----------------------------------------------------------------------------------------------------------------|
| hutool-aop         | JDK dynamic proxy encapsulation, providing aspect support without an IoC container                             |
| hutool-bloomFilter | Bloom filter, with several Hash-based Bloom filter implementations                                             |
| hutool-cache       | A simple cache implementation                                                                                  |
| hutool-core        | Core module: bean operations, date handling, various Utils, etc.                                               |
| hutool-cron        | Scheduled tasks using Crontab-like expressions                                                                 |
| hutool-crypto      | Encryption/decryption, wrapping symmetric, asymmetric, and digest algorithms                                   |
| hutool-db          | Database operations based on the ActiveRecord pattern                                                           |
| hutool-dfa         | Multi-keyword lookup based on the DFA model                                                                    |
| hutool-extra       | Extension modules wrapping third-party libraries (template engines, mail, Servlet, QR codes, Emoji, FTP, word segmentation, etc.) |
| hutool-http        | HTTP client based on `HttpUrlConnection`                                                                       |
| hutool-log         | Logging facade that auto-detects the log implementation                                                         |
| hutool-script      | Script execution wrapper, e.g. JavaScript                                                                       |
| hutool-setting     | More powerful wrappers for `Setting` config files and `Properties`                                             |
| hutool-system      | System parameter wrappers (JVM info, etc.)                                                                     |
| hutool-json        | JSON implementation                                                                                             |
| hutool-captcha     | Image captcha implementation                                                                                    |
| hutool-poi         | Wrappers for working with Excel and Word via POI                                                               |
| hutool-socket      | NIO/AIO socket wrappers for Java                                                                                |
| hutool-jwt         | JSON Web Token (JWT) implementation                                                                            |
| hutool-ai          | AI large-model wrappers                                                                                         |

Each module can be introduced individually, or all of them can be introduced at once via `hutool-all`.

-------------------------------------------------------------------------------

## 📝Documentation

[📘Chinese documentation](https://doc.hutool.cn/pages/index/)

[📘Chinese back-up documentation](https://plus.hutool.cn/)

[📙API reference](https://plus.hutool.cn/apidocs/)

[🎬Video introduction](https://www.bilibili.com/video/BV1bQ4y1M7d9?p=2)

-------------------------------------------------------------------------------

## 📦Installation

### 🍊Maven

Add the following to the `<dependencies>` section of your `pom.xml`:

```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.8.49</version>
</dependency>
```

### 🍐Gradle

```
implementation 'cn.hutool:hutool-all:5.8.49'
```

### 📥Download the jar

Click the link below and download `hutool-all-X.X.X.jar`:

- [Maven Central](https://repo1.maven.org/maven2/cn/hutool/hutool-all/5.8.49/)

> 🔔️Note:
> Hutool 5.x requires JDK 8+. It has not been tested on Android, so not all utility classes or methods are guaranteed to work there.
> If your project uses JDK 7, please use Hutool 4.x (no longer updated).

### 🚽Compile and install

Download the full project source from the Hutool Gitee homepage — [https://gitee.com/chinabugotech/hutool](https://gitee.com/chinabugotech/hutool) — (either the `v5-master` or `v5-dev` branch), then run the following in the project directory:

```sh
./hutool.sh install
```

You can then use Maven to add it as a dependency.

-------------------------------------------------------------------------------

## 🏗️Contributing

### 🎋Branch description

Hutool's source code is split into two branches:

| branch     | description                                                                                                    |
|------------|----------------------------------------------------------------------------------------------------------------|
| v5-master  | The release branch. Matches the jar published to Maven Central and does not accept any PR or modification.     |
| v5-dev     | The development branch. Points to the next SNAPSHOT version and accepts modifications and PRs.                 |

### 🐞Bug reports and suggestions

When submitting an issue, please state which JDK version, Hutool version, and related dependency versions you are using.

- [Gitee issue](https://gitee.com/chinabugotech/hutool/issues)
- [Github issue](https://github.com/chinabugotech/hutool/issues)
- [Gitcode issue](https://gitcode.com/chinabugotech/hutool/issues)

### 🧬Contribution steps

1. Fork the project to your own account on Gitee, GitHub, or Gitcode.
2. Clone your fork to your local machine.
3. Make your changes (remember to work on the `v5-dev` branch).
4. Commit and push to your fork (`v5-dev` branch).
5. On the Gitee/GitHub/Gitcode homepage you will see a "pull request" button — click it, fill in a description, and submit.
6. Wait for the maintainer to merge it.

### 📐PR principles

Hutool welcomes anyone to contribute. However, the maintainer is a bit of a perfectionist, so PRs (pull requests) need to meet a few conventions:

1. Comments must be complete. In particular, every new method should follow the Java documentation convention and describe the method, its parameters, and its return value. Add unit tests where necessary — and if you'd like, add your name.
2. Indentation follows the Eclipse default (tab). Please keep it.
3. Newly added methods must not call third-party library methods — Hutool follows a no-dependency principle (except when adding tools to the `extra` module).
4. Please open pull requests against the `v5-dev` branch. In Hutool 5.x and later, `v5-master` is the release branch (already published to Maven Central) and does not accept PRs or modifications.
5. If we close your issue or PR, please don't be surprised — it is how we keep things tidy. You are still welcome to keep discussing; we will reopen it once a conclusion is reached.

### 📖Documentation source code

[Documentation source code](https://gitee.com/loolly_admin/hutool-doc-handy)

-------------------------------------------------------------------------------

## ⭐Star Hutool

[![Stargazers over time](https://starchart.cc/chinabugotech/hutool.svg)](https://starchart.cc/chinabugotech/hutool)
