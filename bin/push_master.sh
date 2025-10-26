#!/bin/bash

#
# Copyright (c) 2013-2025 Hutool Team and hutool.cn
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# show Hutool logo
"$(dirname "${BASH_SOURCE[0]}")"/logo.sh

echo -e "\033[32mCheckout to v7-master\033[0m"
git checkout v7-master

echo -e "\033[32mMerge v7-dev branch\033[0m"
git merge v7-dev -m 'Prepare release'

echo -e "\033[32mPush to Github(origin) v7-master\033[0m"
git push origin v7-master
echo -e "\033[32mPush to Gitee v7-master\033[0m"
git push osc v7-master
echo -e "\033[32mPush to Gitcode v7-master\033[0m"
git push gitcode v7-master
