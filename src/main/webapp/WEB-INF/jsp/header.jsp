<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="container">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="${ctx}/rest/"><img src="${ctx}/img/library.png" width="25px" height="25px" />  MELM</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">Libraries</a>
          <ul class="dropdown-menu">
            <li><a href="${ctx}/rest/libraries"><span class="glyphicon glyphicon-list"></span>  List</a></li>
            <li class="divider"></li>
            <li><a href="${ctx}/rest/libraries/add"><span class="glyphicon glyphicon-plus"></span>  Add</a></li>
            <li class="divider"></li>
            <li><a href="${ctx}/rest/libraries/import"><span class="glyphicon glyphicon-cloud-upload"></span>  Import</a></li>
          </ul>
        </li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">Icons</a>
          <ul class="dropdown-menu">
            <li><a href="${ctx}/rest/icons"><span class="glyphicon glyphicon-list"></span>  List</a></li>
            <li class="divider"></li>
            <li><a href="${ctx}/rest/icons/add"><span class="glyphicon glyphicon-plus"></span>  Add</a></li>
          </ul>
        </li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="${ctx}/logout"><span class="glyphicon glyphicon-log-out"></span>  Log-out</a></li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>