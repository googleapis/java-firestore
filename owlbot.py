# Copyright 2018 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""This script is used to synthesize generated parts of this library."""

import synthtool as s
import synthtool.gcp as gcp
import synthtool.languages.java as java

protobuf_header = "// Generated by the protocol buffer compiler.  DO NOT EDIT!"
# License header
license_header = """/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
"""
bad_license_header = """/\\*
 \\* Copyright 2018 Google LLC
 \\*
 \\* Licensed under the Apache License, Version 2.0 \\(the "License"\\); you may not use this file except
 \\* in compliance with the License. You may obtain a copy of the License at
 \\*
 \\* http://www.apache.org/licenses/LICENSE-2.0
 \\*
 \\* Unless required by applicable law or agreed to in writing, software distributed under the License
 \\* is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 \\* or implied. See the License for the specific language governing permissions and limitations under
 \\* the License.
 \\*/
"""
for library in s.get_staging_dirs():
  # put any special-case replacements here
  service = "firestore"
  version = "v1"
  s.replace(
      f'owl-bot-staging/v1/proto-google-cloud-{service}-{version}-java/src/**/*.java',
      protobuf_header,
      f'{license_header}{protobuf_header}'
  )

  service == "firestore-admin"
  s.replace(
      f'owl-bot-staging/v1/grpc-google-cloud-{service}-{version}-java/src/**/*.java',
      bad_license_header,
      license_header
  )
  s.replace(
      f'owl-bot-staging/v1/proto-google-cloud-{service}-{version}-java/src/**/*.java',
      bad_license_header,
      license_header
  )
  s.move(library)

s.remove_staging_dirs()

java.common_templates(excludes=[
    # firestore uses a different project for its integration tests
    # due to the default project running datastore
    '.kokoro/presubmit/integration.cfg',
    '.kokoro/presubmit/graalvm-native.cfg',
    '.kokoro/presubmit/samples.cfg',
    '.kokoro/nightly/integration.cfg',
    '.kokoro/nightly/java11-integration.cfg',
    '.kokoro/nightly/samples.cfg'
])