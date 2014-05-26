<nav id="main-nav" class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="container">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse">
        <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="${ctx}/rest/"><img alt="Pulse Collection" src="${ctx}/img/library_ffdc00.png" />Pulse Collection</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="navbar-collapse">
      <ul class="nav navbar-nav">
        <li class="dropdown"><a href="#" data-toggle="dropdown">Libraries <b class="caret"></b></a>
          <ul class="dropdown-menu">
            <li><a href="${ctx}/rest/libraries">List</a></li>
            <li class="divider"></li>
            <li><a href="${ctx}/rest/libraries/add">Add</a></li>
            <li class="divider"></li>
            <li><a href="${ctx}/rest/libraries/import">Import</a></li>
          </ul></li>
        <li class="dropdown"><a href="#" data-toggle="dropdown">Icons <b class="caret"></b></a>
          <ul class="dropdown-menu">
            <li><a href="${ctx}/rest/icons">List</a></li>
            <li class="divider"></li>
            <li><a href="${ctx}/rest/icons/add">Add</a></li>
          </ul></li>
      </ul>
      <ul id="btn-logout" class="nav navbar-nav navbar-right">
        <li><a href="${ctx}/logout">Log-out</a></li>
      </ul>
    </div>
    <!-- /.navbar-collapse -->
  </div>
  <!-- /.container-fluid -->
</nav>