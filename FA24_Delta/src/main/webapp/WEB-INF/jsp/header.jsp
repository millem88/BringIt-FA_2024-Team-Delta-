<!-- Author: Abdinasir Aidrus (Nas) -->
<!-- A navbar component with a list of links using bootstrap 4. -->
<header>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Navbar</title>

    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <!-- local css/js files-->
    <link rel="stylesheet" href="css/style.css">
    
</head>
<body>
    <header>
        <nav class="navbar navbar-expand-md navbar-light container">
          <a class="navbar-brand text-white" href="#"><img class="navbar__logo" src="" alt=""></a>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
            
          </button>
          <div class="collapse navbar-collapse d-md-flex justify-content-md-end text-uppercase" id="navbarNav">
            <ul class="navbar-nav">
              <li class="nav-item">
                <a class="nav-link text-white" href="#">Create Event</a>
              </li>
              <li class="nav-item ml-lg-4">
                <a class="nav-link text-white" href="#">My Events</a>
              </li>
           <li class="nav-item  ml-lg-4">
                <a class="nav-link text-white" href="#">Search Events</a>
              </li>
              <li class="nav-item  ml-lg-4">
                <a class="nav-link text-white" href="#">Logout</a>
              </li>
            </ul>
          </div>
        </nav>
      </header>

      <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>