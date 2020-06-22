#!/bin/bash

# Copyright 2020 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

set -euo pipefail
IFS=$'\n\t'

# Variables
SDK_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
TEMP_DIR=`mktemp -d`

# deletes the temp directory on exit
function cleanup {
  rm -rf "$TEMP_DIR"
  echo "Deleted temp working directory $TEMP_DIR"
}

# register the cleanup function to be called on the EXIT signal
trap cleanup EXIT

# Clone googleapis to temp directory.
cd "$TEMP_DIR"
git clone --depth 1 https://github.com/googleapis/googleapis.git

# Tell synthtool to use the cloned googleapi repo.
SYNTHTOOL_GOOGLEAPIS="$TEMP_DIR/googleapis"

# Patch bundle.proto to local googleapi repo.
mkdir "$SYNTHTOOL_GOOGLEAPIS/firestore"
cd "$SDK_DIR"
cp proto-google-cloud-firestore-v1/src/main/proto/firestore/* "$SYNTHTOOL_GOOGLEAPIS/firestore/"

# Generate code with synthtool.
python3 -m synthtool
