/*
 * Sonar SCM Activity Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.scmactivity;

import org.apache.commons.codec.digest.DigestUtils;
import org.sonar.api.BatchExtension;

import java.io.IOException;
import java.util.regex.Pattern;

public class Sha1Generator implements BatchExtension {
  private static final Pattern LINE_BREAKS = Pattern.compile("(\r)?+\n|\r");

  public Sha1Generator() {
  }

  public String find(String fileContent) throws IOException {
    fileContent = LINE_BREAKS.matcher(fileContent).replaceAll("\n");

    return DigestUtils.shaHex(fileContent);
  }
}
