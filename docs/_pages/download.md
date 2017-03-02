---
layout: default
title: Download & Install
permalink: /download/
---

<div class="jumbotron">
  <div class="table-responsive">
    <table class="table borderless">
      <tr>
        <th>Version</th>
        <th>Released</th>
        <th></th>
      </tr>
      <tr class="active">
        <td>0.39</td>
        <td>
          <div>31.1.2017</div>
          <div><a href="">Release notes</a></div>
        </td>
        <td>
          <span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
          <button class="button btn-link btn-lg">emuStudio-0.39.zip</button>
        </td>
      </tr>
      <tr>
        <td>0.38</td>
        <td>
          <div>24.10.2011</div>
          <div><a href="https://sourceforge.net/projects/emustudio/files/dist/changelog-0.38.txt/download"
                  target="_blank">Release notes</a></div>
        </td>
        <td>
          <span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
          <a href="https://sourceforge.net/projects/emustudio/files/dist/emuStudio-0.38b.zip/download" 
             class="button btn-link btn-lg"
             role="button"
             target="_blank">emuStudio-0.38.zip</a>
        </td>
      </tr>
    </table>
  </div>
  <p>
    Older versions of emuStudio are available at
     <a href="https://sourceforge.net/projects/emustudio/files/dist/" target="_blank">SourceForge</a>.
  </p>
</div>


# Installation

The first step is to unpack the <code>emuStudio-xx.zip</code> file into location where you want to have emuStudio
installed. The ZIP file contains the whole emuStudio with all official virtual computers, including examples and
some of the sample images.
 
In order to run emuStudio, run the following command from console:

<samp>java -jar emuStudio.jar</samp>

<div class="alert alert-info">
  <span class="glyphicon glyphicon-exclamation-sign"></span> It is required to install a
  <a href="http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html" target="_blank">Java Runtime Environment</a>
  of version 8 or later.
</div>


## Directory structure

The directory structure of emuStudio is well-defined, and strict.

<table class="table borderless">
  <tr>
    <td class="tree"><span class="treeitem"><code>config/</code></span></td>
    <td>Configuration files of virtual computers.</td>
  </tr>
  <tr>
    <td class="tree"><span class="treeitem"><code>compilers/</code></span></td>
    <td>Compiler plug-ins</td>
  </tr>
  <tr>
    <td class="tree"><span class="treeitem"><code>cpu/</code></span></td>
    <td>CPU plug-ins</td>
  </tr>
  <tr>
    <td class="tree"><span class="treeitem"><code>devices/</code></span></td>
    <td>Devices plug-ins</td>
  </tr>
  <tr>
    <td class="tree"><span class="treeitem"><code>mem/</code></span></td>
    <td>Memory plug-ins</td>
  </tr>
  <tr>
    <td class="tree"><span class="treeitem"><code>lib/</code></span></td>
    <td>Shared run-time libraries</td>
  </tr>
</table>

If you want to use a custom virtual computer in emuStudio, it's plug-ins and shared libraries must be put into appropriate
subdirectories. Then, the abstract schema editor will find new plug-ins right after fresh start of emuStudio, and
they can be used immediately. 