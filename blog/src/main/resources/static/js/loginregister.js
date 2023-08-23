function registerSlideUp() {
	document.getElementById('register').style = 'display: block';
	document.getElementById('login').style = 'display: none'
}

function loginSlideUp() {
	document.getElementById('login').style = "display: block";
	document.getElementById('register').style = "display: none";
}


// Function to check Whether both passwords
// is same or not.
function checkPassword(form) {
	const password1 = form.register_user_password.value;
	const password2 = form.confirm_password.value;

	// If password not entered
	if (password1 == '')
		alert("Please enter Password");

	// If confirm password not entered
	else if (password2 == '')
		alert("Please enter confirm password");

	// If Not same return False.    
	else if (password1 != password2) {
		alert("\nPassword did not match: Please try again...")
		return false;
	}

	// If same return True.
	else {
		alert("Password Match: Welcome to the blog!")
		return true;
	}
}