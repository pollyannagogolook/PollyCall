"""
WSGI config for callsite project.

It exposes the WSGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/5.0/howto/deployment/wsgi/
"""

import os

from django.core.wsgi import get_wsgi_application
import dotenv

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "callsite.settings")

application = get_wsgi_application()

dotenv.load_dotenv(
    os.path.join(os.path.dirname(__file__), '.env')
)
